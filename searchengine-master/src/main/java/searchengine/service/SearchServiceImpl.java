package searchengine.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import searchengine.config.SitesList;
import searchengine.dto.search.*;
import searchengine.model.IndexEntity;
import searchengine.model.LemmaEntity;
import searchengine.model.PageEntity;
import searchengine.model.SiteEntity;
import searchengine.repository.IndexRepository;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;

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
    private static final String REGEX_SPLIT = " +";
    private static final String[] SELECTION = {"<b>", "</b>"};

    @SneakyThrows
    @Override
    public SearchRepository search(String query, String site, int offset, int limit) {

        if (query.isEmpty())
            return new SearchError();
        Set<String> querySplit = new HashSet<>(Arrays.asList(query.split(REGEX_SPLIT)));
        SearchLemmas searchLemmas = SearchLemmas.getLuceneMorphology();
        List<String> baseFormLemmas = new ArrayList<>();

        querySplit.stream()
                .filter(q -> q.matches(REGEX))
                .filter(q -> searchLemmas.baseFormLemma(q.toLowerCase()) != null)
                .forEach(q -> baseFormLemmas.add(searchLemmas.baseFormLemma(q.toLowerCase(Locale.ROOT))));

        Optional<SiteEntity> siteEntity = siteRepository.findByUrl(site);

        List<LemmaEntity> lemmaEntityList = searchLemmas(baseFormLemmas, siteEntity.get());
        if (lemmaEntityList.isEmpty())
            return new LemmaError();

        lemmaEntityList.sort(Comparator.comparing(LemmaEntity::getFrequency));
        List<PageEntity> pagesList = searchPages(lemmaEntityList);
        List<PageSettings> data = new ArrayList<>();
        float allRank = 0;
        for (PageEntity page : pagesList) {
            allRank += relevancePage(page, lemmaEntityList);
        }
        for (PageEntity page : pagesList) {
            Document document = Jsoup.parse(page.getContent());
            String title = document.title();
            String[] pages = document.body().text().split(REGEX_SPLIT);
            int snippetPosition = -1;
            for (int i = 0; i < pages.length; i++) {
                for (LemmaEntity lemmaEntity : lemmaEntityList) {
                    if (!pages[i].toLowerCase().matches(REGEX))
                        continue;
                    if (lemmaEntity.getLemma().toLowerCase()
                            .equals(searchLemmas.baseFormLemma(pages[i].toLowerCase()))) {
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
            PageSettings pageSettings = new PageSettings();
            pageSettings.setRelevance(relevance / allRank);
            pageSettings.setUri(page.getPath());
            pageSettings.setTitle(title);
            pageSettings.setSite(siteEntity.get().getUrl());
            pageSettings.setSnippet(snippet.toString());
            pageSettings.setSiteName(siteEntity.get().getName());
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
        float pageCount = siteEntity == null ? pageRepository.count() :
                pageRepository.countBySiteId(siteEntity);
        for (String lemma : baseFormLemmas) {
            if (siteEntity == null)
                lemmaEntityList.addAll(lemmaRepository.findAllByLemma(lemma));
            else
                lemmaEntityList.add(lemmaRepository.findByLemmaAndSiteId(lemma, siteEntity.getId()));
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
                Optional<IndexEntity> indexEntity = indexRepository
                        .findByLemmaIdAndPageId(lemmaEntityList.get(j), pagesList.get(i));
                if (indexEntity.isPresent()) {
                    continue;
                }
                pagesList.remove(i);
                i = 0;
            }
        }
        return pagesList;
    }

    private float relevancePage(PageEntity page, List<LemmaEntity> lemmaEntityList) {
        float rank = 0;
        for (LemmaEntity lemmaEntity : lemmaEntityList) {
            Optional<IndexEntity> indexEntity = indexRepository
                    .findByLemmaIdAndPageId(lemmaEntity, page);
            if (indexEntity.isPresent()) {
                rank += indexEntity.get().getRank();
            }
        }
        return rank;
    }
}
