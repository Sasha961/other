package searchengine.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import searchengine.config.SitesList;
import searchengine.dto.PageAndRank;
import searchengine.dto.Responce.search.EmptyRequestError;
import searchengine.dto.Responce.search.SearchLemmaError;
import searchengine.dto.Responce.search.SearchPageSettings;
import searchengine.dto.Responce.search.SearchSettings;
import searchengine.model.Index;
import searchengine.model.Lemma;
import searchengine.model.Site;
import searchengine.repository.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
                .filter(q -> q.matches(REGEX)
                        && searchLemmas.baseFormLemma(q.toLowerCase()) != null
                        && lemmaRepository.countByLemma(q) > 0)
                .forEach(q -> baseFormLemmas.add(searchLemmas.baseFormLemma(q.toLowerCase())));

        if (baseFormLemmas.isEmpty()) {
            return new SearchLemmaError();
        }

        List<Site> sites = new ArrayList<>();
        if (site.isEmpty()) {
            sitesList.getSites().stream()
                    .map(s -> siteRepository.findByUrl(s.getUrl()))
                    .forEach(s -> sites.add(s.get()));
        } else
            sites.add(siteRepository.findByUrl(site.get()).get());
        List<PageAndRank> pagesList = new ArrayList<>();
        SearchSettings searchSettings = new SearchSettings();

        for (Site siteEntity : sites) {
            List<Lemma> lemmaEntityList = baseFormLemmas.stream()
                    .map(baseLemma -> lemmaRepository.findByLemmaAndSiteId(baseLemma, siteEntity.getId()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            pagesList.addAll(searchPages(lemmaEntityList));
        }
        List<SearchPageSettings> data = new ArrayList<>(dataList(pagesList, baseFormLemmas, searchLemmas));
        data.sort(Comparator.comparing(d -> d.relevance, Comparator.reverseOrder()));
        searchSettings.setResult(true);
        searchSettings.setData(data);
        searchSettings.setCount(data.size());
        return searchSettings;
    }

    private List<SearchPageSettings> dataList(List<PageAndRank> pagesList,
                                              List<String> baseFormLemmas,
                                              SearchLemmas searchLemmas) {
        List<SearchPageSettings> data = new ArrayList<>();
        for (PageAndRank page : pagesList) {
            Document document = Jsoup.parse(page.getPage().getContent());
            String title = document.title();
            String[] pages = document.body().text().split(REGEX_SPLIT_SPACE);
            int snippetPosition = selectionAndSnippetPosition(pages, baseFormLemmas, searchLemmas);
            StringBuilder snippet = searchSnippet(pages, snippetPosition);
            SearchPageSettings pageSettings = new SearchPageSettings();
            pageSettings.setRelevance(page.getRank() / allRank);
            pageSettings.setUri(page.getPage().getPath());
            pageSettings.setTitle(title);
            pageSettings.setSite(page.getPage().getSiteId().getUrl());
            pageSettings.setSnippet(snippet.toString());
            pageSettings.setSiteName(page.getPage().getSiteId().getName());
            if (data.contains(pageSettings)) {
                continue;
            }
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
                                            List<String> baseFormLemmas,
                                            SearchLemmas searchLemmas) {
        int snippetPosition = -1;
        for (int i = 0; i < pages.length; i++) {
            for (String lemma : baseFormLemmas) {
                if (!pages[i].toLowerCase().matches(REGEX))
                    break;
                if (lemma.equals(searchLemmas.baseFormLemma(pages[i].toLowerCase()))) {
                    pages[i] = SELECTION[0] + pages[i] + SELECTION[1];
                    if (snippetPosition == -1)
                        snippetPosition = i;
                }
            }
        }
        return snippetPosition;
    }

    private boolean frequentMatch(Lemma lemma, float pageCount, int length) {
        float lemmaCount = lemma.getFrequency();
        float percent = (lemmaCount / pageCount) * 100;
        return percent > 85 && length < 1;
    }

    private List<PageAndRank> searchPages(List<Lemma> lemmaEntityList) {
        List<PageAndRank> pagesList = new ArrayList<>();
        lemmaEntityList.sort(Comparator.comparing(Lemma::getFrequency));
        List<Index> indexEntitiesList = indexRepository.findAllByLemmaId(lemmaEntityList.get(0));
        float pageCount = pageRepository.countBySiteId(lemmaEntityList.get(0).getSite());
        for (int i = 0; i < indexEntitiesList.size(); i++) {
            for (Lemma lemma : lemmaEntityList) {
                float rank = 0;
                if (indexRepository.findByLemmaIdAndPageId(lemma,
                        indexEntitiesList.get(i).getPageId()).isEmpty() ||
                        frequentMatch(lemma, pageCount, indexEntitiesList.size())) {
                    indexEntitiesList.remove(i);
                    i--;
                    continue;
                }
                allRank += indexEntitiesList.get(i).getRank();
                rank += indexEntitiesList.get(i).getRank();
                PageAndRank pageAndRank = new PageAndRank();
                pageAndRank.setPage(indexEntitiesList.get(i).getPageId());
                pageAndRank.setRank(rank);
                pagesList.add(pageAndRank);
            }
        }
        return pagesList;
    }
}
