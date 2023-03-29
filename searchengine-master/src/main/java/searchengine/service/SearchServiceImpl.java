package searchengine.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import searchengine.config.SitesList;
import searchengine.dto.Responce.search.EmptyRequestError;
import searchengine.dto.Responce.search.SearchLemmaError;
import searchengine.dto.Responce.search.SearchPageSettings;
import searchengine.dto.Responce.search.SearchSettings;
import searchengine.model.Index;
import searchengine.model.Lemma;
import searchengine.model.Page;
import searchengine.model.Site;
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

    private static final int SHIFT = 20;
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
        List<Optional<Site>> sites = new ArrayList<>();
        if (site.isEmpty()) {
            sitesList.getSites().stream()
                    .map(s -> siteRepository.findByUrl(s.getUrl()))
                    .forEach(sites::add);
        } else {
            sites.add(siteRepository.findByUrl(site.get()));
        }
        float pageCount = 0;
        List<Page> pagesList = new ArrayList<>();
        List<SearchPageSettings> data = new ArrayList<>();
        SearchSettings searchSettings = new SearchSettings();
        for (Optional<Site> siteEntity1 : sites) {
            List<Lemma> lemmaEntityList = new ArrayList<>(searchLemmas(baseFormLemmas, siteEntity1.get(), pageCount));
            if (lemmaEntityList.isEmpty()) {
                return new SearchLemmaError();
            }
            lemmaEntityList.sort(Comparator.comparing(Lemma::getFrequency));
            pagesList.addAll(searchPages(lemmaEntityList));
            data.addAll(dataList(pagesList, lemmaEntityList, searchLemmas));
        }
        data.sort(Comparator.comparing(d -> d.relevance, Comparator.reverseOrder()));
        searchSettings.setResult(true);
        searchSettings.setData(data);
        searchSettings.setCount(pagesList.size());
        return searchSettings;
    }

    private List<SearchPageSettings> dataList(List<Page> pagesList,
                                              List<Lemma> lemmaEntityList,
                                              SearchLemmas searchLemmas) {
        List<SearchPageSettings> data = new ArrayList<>();
        for (Page page : pagesList) {
            Document document = Jsoup.parse(page.getContent());
            String title = document.title();
            String[] pages = document.body().text().split(REGEX_SPLIT_SPACE);
            int snippetPosition = selectionAndSnippetPosition(pages, lemmaEntityList, searchLemmas);
            StringBuilder snippet = searchSnippet(pages, snippetPosition);
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
        return data;
    }

    private StringBuilder searchSnippet(String[] pages, int snippetPosition) {
        StringBuilder snippet = new StringBuilder();
        if (snippetPosition != -1) {
            int shiftRight = (snippetPosition + SHIFT > pages.length ? pages.length - 1 : snippetPosition + SHIFT);
            for (int j = snippetPosition; j < shiftRight; j++)
                snippet.append(pages[j]).append(" ");
        }
        return new StringBuilder(snippet.toString().trim());
    }

    private int selectionAndSnippetPosition(String[] pages,
                                            List<Lemma> lemmaEntityList,
                                            SearchLemmas searchLemmas) {
        int snippetPosition = -1;
        for (int i = 0; i < pages.length; i++) {
            for (Lemma lemmaEntity : lemmaEntityList) {
                if (!pages[i].toLowerCase().matches(REGEX))
                    break;
                if (lemmaEntity.getLemma().equals(searchLemmas.baseFormLemma(pages[i].toLowerCase()))) {
                    pages[i] = SELECTION[0] + pages[i] + SELECTION[1];
                    if (snippetPosition == -1)
                        snippetPosition = i;
                }
            }
        }
        return snippetPosition;
    }

    private List<Lemma> searchLemmas(List<String> baseFormLemmas, Site siteEntity, float pageCount) {
        List<Lemma> finalLemmaEntityList = new ArrayList<>();
        List<Lemma> lemmaEntityList = new ArrayList<>();
        pageCount += pageRepository.countBySiteId(siteEntity);
        baseFormLemmas
                .stream().filter(lemma -> lemmaRepository.findByLemmaAndSiteId(lemma, siteEntity.getId()) != null)
                .forEach(lemma ->
                        lemmaEntityList.add(lemmaRepository.findByLemmaAndSiteId(lemma, siteEntity.getId())));
        for (Lemma lemmaEntity : lemmaEntityList) {
            float lemmaCount = lemmaEntity.getFrequency();
            float percent = (lemmaCount / pageCount) * 100;
            if (percent > 85 && finalLemmaEntityList.size() >= 1)
                continue;
            finalLemmaEntityList.add(lemmaEntity);
        }
        return finalLemmaEntityList;
    }

    private List<Page> searchPages(List<Lemma> lemmaEntityList) {
        List<Index> indexEntitiesList = indexRepository.findAllByLemmaId(lemmaEntityList.get(0));
        List<Page> pagesList = new ArrayList<>();
        indexEntitiesList.stream()
                .filter(index -> !pagesList.contains(index.getPageId()))
                .forEach(index -> pagesList.add(index.getPageId()));
        for (int i = 0; i < pagesList.size(); i++) {
            for (int j = 1; j < lemmaEntityList.size(); j++) {
                Optional<Index> indexEntity = indexRepository
                        .findByLemmaIdAndPageId(lemmaEntityList.get(j), pagesList.get(i));
                if (indexEntity.isPresent()) {
                    allRank += relevancePage(indexEntity.get().getPageId(), lemmaEntityList);
                    continue;
                }
                pagesList.remove(i);
                i--;
            }
        }
        return pagesList;
    }

    private float relevancePage(Page page, List<Lemma> lemmaEntityList) {
        float rank = 0;
        for (Lemma lemmaEntity : lemmaEntityList) {

            Optional<Index> rankLemma = indexRepository.findByLemmaIdAndPageId(lemmaEntity, page);

            rank += rankLemma.get().getRank();
        }
        return rank;
    }
}
