package searchengine.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private static final int SHIFT = 35;
    private static final String REGEX = "[ёа-я]+";
    private static final String NOT_REGEX = "[^ёа-я]+";
    private static final String REGEX_SPLIT_SPACE = " +";
    private static final String[] SELECTION = {"<b>", "</b>"};
    private float allRank = 0;
    private int limitPage = 0;
    private String checkQuery = "";

    @Override
    public ResponseEntity<SearchRepository> search(String query, Optional<String> site, int offset, int limit) {
        if (query.isEmpty()) return new ResponseEntity<>(new EmptyRequestError(), HttpStatus.BAD_REQUEST);
        if (checkQuery.isEmpty() || !checkQuery.equals(query)) limitPage = limit;
        else limitPage += limit;
        checkQuery = query;
            SearchLemmas searchLemmas;
        try {
            searchLemmas = SearchLemmas.getLuceneMorphology();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Set<String> querySplit = new HashSet<>(Arrays.asList(query.split(REGEX_SPLIT_SPACE)));
        List<String> baseFormLemmas = new ArrayList<>();
        querySplit.stream()
                .filter(q -> q.toLowerCase().matches(REGEX))
                .map(searchLemmas::baseFormLemma)
                .filter(q -> q != null && lemmaRepository.countByLemma(q) > 0)
                .forEach(baseFormLemmas::add);
        if (baseFormLemmas.size() < 1) return new ResponseEntity<>(new SearchLemmaError(), HttpStatus.BAD_REQUEST);
        List<Site> sites = new ArrayList<>();
        if (site.isEmpty()) {
            sitesList.getSites().stream()
                    .map(s -> siteRepository.findByUrl(s.getUrl()))
                    .forEach(s -> sites.add(s.get()));
        } else sites.add(siteRepository.findByUrl(site.get()).get());
        List<PageAndRank> pagesList = new ArrayList<>();
        for (Site siteEntity : sites) {
            List<Lemma> lemmaEntityList = baseFormLemmas.stream()
                    .map(baseLemma -> lemmaRepository.findByLemmaAndSiteId(baseLemma, siteEntity.getId()))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            if (lemmaEntityList.size() < 1) continue;
            pagesList.addAll(searchPagesAndRank(lemmaEntityList));
        }
        List<SearchPageSettings> data = new ArrayList<>(dataList(pagesList, baseFormLemmas, searchLemmas));
        data.sort(Comparator.comparing(d -> d.relevance, Comparator.reverseOrder()));
        SearchSettings searchSettings = new SearchSettings();
        searchSettings.setResult(true);
        searchSettings.setData(data.subList(offset, Math.min(limitPage, data.size())));
        searchSettings.setCount(data.size());
        return new ResponseEntity<>(searchSettings, HttpStatus.OK);
    }

    private List<SearchPageSettings> dataList(List<PageAndRank> pagesList,
                                              List<String> baseFormLemmas,
                                              SearchLemmas searchLemmas) {
        List<SearchPageSettings> data = new ArrayList<>();
        for (PageAndRank page : pagesList) {
            Document document = Jsoup.parse(page.getPage().getContent());
            String title = document.title().isBlank() ? page.getPage().getSiteId().getName() : document.title();
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
            if (data.contains(pageSettings)) continue;
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
                String clearPage = pages[i].toLowerCase().replaceAll(NOT_REGEX, "");
                if (!clearPage.matches(REGEX))
                    break;
                if (lemma.toLowerCase().equals(searchLemmas.baseFormLemma(clearPage))) {
                    pages[i] = SELECTION[0] + pages[i] + SELECTION[1];
                    if (snippetPosition == -1)
                        snippetPosition = i;
                }
            }
        }
        return snippetPosition;
    }

    private List<Lemma> frequentMatch(List<Lemma> lemmaEntityList) {
        float pageCount = pageRepository.countBySiteId(lemmaEntityList.get(0).getSite());
        List<Lemma> lemmas = new ArrayList<>();
        for (Lemma lemma : lemmaEntityList) {
            float lemmaCount = lemma.getFrequency();
            float percent = (lemmaCount / pageCount) * 100;
            if (percent < 85 || lemmas.size() < 1) {
                lemmas.add(lemma);
            }
        }
        return lemmas;
    }

    private List<PageAndRank> addPageAndRank(List<Lemma> lemmaList, List<PageAndRank> pageAndRankList) {
        if (lemmaList.size() < 2) {
            return pageAndRankList;
        }
        for (int i = 1; i < lemmaList.size(); i++) {
            for (int j = 0; j < pageAndRankList.size(); j++) {
                Optional<Index> index = indexRepository
                        .findByLemmaIdAndPageId(lemmaList.get(i), pageAndRankList.get(j).getPage());
                if (index.isEmpty()) {
                    pageAndRankList.remove(j);
                    j--;
                } else {
                    pageAndRankList.get(j).setRank(pageAndRankList.get(j).getRank() + index.get().getRank());
                    allRank += index.get().getRank();
                }
            }
        }
        return pageAndRankList;
    }

    private List<PageAndRank> searchPagesAndRank(List<Lemma> lemmaEntityList) {
        List<PageAndRank> pagesList = new ArrayList<>();
        lemmaEntityList.sort(Comparator.comparing(Lemma::getFrequency));
        List<Lemma> lemmaList = frequentMatch(lemmaEntityList);
        List<Index> indexList = indexRepository.findAllByLemmaId(lemmaEntityList.get(0));
        for (Index index : indexList) {
            PageAndRank pageAndRank = new PageAndRank();
            pageAndRank.setRank(index.getRank());
            pageAndRank.setPage(index.getPageId());
            pagesList.add(pageAndRank);
        }
        return addPageAndRank(lemmaList, pagesList);
    }
}
