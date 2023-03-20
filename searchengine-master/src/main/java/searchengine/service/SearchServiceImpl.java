package searchengine.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import searchengine.config.SitesList;
import searchengine.dto.search.*;
import searchengine.model.IndexEntity;
import searchengine.model.LemmaEntity;
import searchengine.model.PageEntity;
import searchengine.model.SiteEntity;
import searchengine.repository.*;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    final LemmaRepository lemmaRepository;
    final PageRepository pageRepository;
    final SiteRepository siteRepository;
    final IndexRepository indexRepository;
    final SitesList sitesList;

    private static final int SHIFT = 10;
    private static final String REGEX = "[а-я]+";
    private static final String REGEX_SPLIT_SPACE = " +";
    private static final String[] SELECTION = {"<b>", "</b>"};

    private float allRank = 0;

    @Override
    public SearchRepository search(String query, Optional<String> site, int offset, int limit) {

        if (query.isEmpty()) {
            return new EmptyRequestError();
        }
        SearchLemmas searchLemmas;
        try {
            searchLemmas = SearchLemmas.getLuceneMorphology();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Set<String> querySplit = new HashSet<>(Arrays.asList(query.split(REGEX_SPLIT_SPACE)));
        List<String> baseFormLemmas = new ArrayList<>();
        querySplit.stream()
                .filter(q -> q.matches(REGEX) && searchLemmas.baseFormLemma(q.toLowerCase()) != null)
                .forEach(q -> baseFormLemmas.add(searchLemmas.baseFormLemma(q.toLowerCase())));

        List<LemmaEntity> lemmaEntityList = new ArrayList<>();
        if (site.isEmpty()) {
            lemmaEntityList.addAll(searchLemmas(baseFormLemmas, null));
        } else {
            Optional<SiteEntity> siteEntity = siteRepository.findByUrl(site.get());
            lemmaEntityList.addAll(searchLemmas(baseFormLemmas, siteEntity.get()));
        }
        if (lemmaEntityList.isEmpty()) {
            return new SearchLemmaError();
        }
        lemmaEntityList.sort(Comparator.comparing(LemmaEntity::getFrequency));
        List<PageEntity> pagesList = searchPages(lemmaEntityList);
        List<SearchPageSettings> data = new ArrayList<>();
        for (PageEntity page : pagesList) {
            Document document = Jsoup.parse(page.getContent());
            String title = document.title();
            String[] pages = document.body().text().split(REGEX_SPLIT_SPACE);
            int snippetPosition = -1;
            for (int i = 0; i < pages.length; i++) {
                for (LemmaEntity lemmaEntity : lemmaEntityList) {
                    if (!pages[i].toLowerCase().matches(REGEX))
                        break;
                    if (lemmaEntity.getLemma().equals(searchLemmas.baseFormLemma(pages[i].toLowerCase()))) {
                        pages[i] = SELECTION[0] + pages[i] + SELECTION[1];
                        if (snippetPosition == -1)
                            snippetPosition = i;
                    }
                }
            }
            StringBuilder snippet = new StringBuilder();
            if (snippetPosition != -1) {
                int shiftLeft = (Math.max(snippetPosition - SHIFT, 0));
                int shiftRight = (snippetPosition + SHIFT > pages.length ? pages.length - 1 : snippetPosition + SHIFT);
                for (int j = shiftLeft; j < shiftRight; j++)
                    snippet.append(pages[j]).append(" ");
            }
            snippet = new StringBuilder(snippet.toString().trim());
            float relevance = relevancePage(page, lemmaEntityList);
            SearchPageSettings pageSettings = new SearchPageSettings();
            pageSettings.setRelevance(relevance / allRank);
            pageSettings.setUri(page.getPath());
            pageSettings.setTitle(title);
            pageSettings.setSite(page.getSiteId().getUrl());
            pageSettings.setSnippet(snippet.toString());
            pageSettings.setSiteName(page.getSiteId().getName());
            data.add(pageSettings);
        }
        data.sort(Comparator.comparing(d -> d.relevance, Comparator.reverseOrder()));
        SearchSettings searchSettings = new SearchSettings();
        searchSettings.setResult(true);
        searchSettings.setData(data);
        searchSettings.setCount(pagesList.size());
        return searchSettings;
    }

    private List<LemmaEntity> searchLemmas(List<String> baseFormLemmas, SiteEntity siteEntity) {
        List<LemmaEntity> finalLemmaEntityList = new ArrayList<>();
        List<LemmaEntity> lemmaEntityList = new ArrayList<>();
        float pageCount;
        if (Optional.ofNullable(siteEntity).isEmpty()) {
            pageCount = pageRepository.count();
            baseFormLemmas.forEach(lemma -> lemmaEntityList.addAll(lemmaRepository.findAllByLemma(lemma)));
        } else {
            pageCount = pageRepository.countBySiteId(siteEntity);
            baseFormLemmas
                    .stream().filter(lemma -> lemmaRepository.findByLemmaAndSiteId(lemma, siteEntity.getId()) != null)
                    .forEach(lemma ->
                            lemmaEntityList.add(lemmaRepository.findByLemmaAndSiteId(lemma, siteEntity.getId())));
        }

        for (LemmaEntity lemmaEntity : lemmaEntityList) {
            float lemmaCount = lemmaEntity.getFrequency();
            float percent = (lemmaCount / pageCount) * 100;
            if (percent > 50)
                continue;
            finalLemmaEntityList.add(lemmaEntity);
        }
        return finalLemmaEntityList;
    }

    private List<PageEntity> searchPages(List<LemmaEntity> lemmaEntityList) {
        List<IndexEntity> indexEntitiesList = indexRepository.findAllByLemmaId(lemmaEntityList.get(0));
        List<PageEntity> pagesList = new ArrayList<>();
        indexEntitiesList.stream()
                .filter(indexEntity -> !pagesList.contains(indexEntity.getPageId()))
                .forEach(indexEntity -> pagesList.add(indexEntity.getPageId()));

        for (int i = 0; i < pagesList.size(); i++) {
            for (int j = 1; j < lemmaEntityList.size(); j++) {
                Optional<PageEntity> pageEntity = Optional.ofNullable(indexRepository
                        .findByLemmaIdAndPageId(lemmaEntityList.get(j), pagesList.get(i)).get().getPageId());
                if (pageEntity.isPresent()) {
                    allRank += relevancePage(pageEntity.get(), lemmaEntityList);
                    continue;
                }
                pagesList.remove(i);
                i--;
            }
        }
        return pagesList;
    }

    private float relevancePage(PageEntity page, List<LemmaEntity> lemmaEntityList) {
        float rank = 0;
        for (LemmaEntity lemmaEntity : lemmaEntityList) {
            Optional<Float> rankLemma = Optional.of(
                    indexRepository.findByLemmaIdAndPageId(lemmaEntity, page).get().getRank());
            rank += rankLemma.get();
        }
        return rank;
    }
}
