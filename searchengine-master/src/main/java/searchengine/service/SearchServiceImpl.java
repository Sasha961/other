package searchengine.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import searchengine.config.SitesList;
import searchengine.dto.search.PageSettings;
import searchengine.dto.search.SearchRepository;
import searchengine.dto.search.SearchSettings;
import searchengine.dto.search.SearchError;
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
public class SearchServiceImpl implements SearchService{

    final LemmaRepository lemmaRepository;
    final PageRepository pageRepository;
    final SiteRepository siteRepository;
    final IndexRepository indexRepository;
    final SitesList sitesList;

    @SneakyThrows
    @Override
    public SearchRepository search(String query, String site, int offset, int limit) {

        Set<String> querySplit = new HashSet<>(Arrays.asList(query.split(" +")));
        SearchLemmas searchLemmas = SearchLemmas.getLuceneMorphology();
        List<String> baseFormLemmas = new ArrayList<>();
        querySplit.stream()
                .filter(q -> searchLemmas.baseFormLemma(q.toLowerCase()) != null)
                .forEach(q -> baseFormLemmas.add(searchLemmas.baseFormLemma(q.toLowerCase(Locale.ROOT))));
        SiteEntity siteEntity = siteRepository.findByUrl(site);
        List<LemmaEntity> lemmaEntityList = searchLemmas(baseFormLemmas, siteEntity);
        if (lemmaEntityList.size() == 0)
            return new SearchError();
       lemmaEntityList.sort(Comparator.comparing(LemmaEntity::getFrequency));
        List<IndexEntity> indexEntitiesList = searchIndex(lemmaEntityList);

        List<PageSettings> data = new ArrayList<>();

        for (IndexEntity indexEntity : indexEntitiesList){

            Optional<PageEntity> pageEntity = pageRepository.findById(indexEntity.getPageId().getId());
            Document document = Jsoup.parse(pageEntity.get().getContent());
            String title = document.title();
            String[] pages = document.body().text().split(" +");

//
            int count = -1;
            for (int i = 0; i < pages.length ; i++){
                    for (LemmaEntity lemmaEntity : lemmaEntityList) {
                        if (pages[i].toLowerCase().matches("[а-я]+")){
                            if (lemmaEntity.getLemma().toLowerCase().equals(searchLemmas.baseFormLemma(pages[i].toLowerCase()))){
                            pages[i] = "<b>" + pages[i] + "</b>";
                            if (count == -1)
                                    count = i;
                        }
                    }
                }
            }
            String snippet = "";
            int revers = 15;
            if (count != -1){

                int a = (count - revers < 0 ? 0 : count - revers);
                int b = (count + revers > pages.length ? pages.length - 1 : count + revers);

                for (int j = a; j < b; j++)
                    snippet += pages[j] + " ";
            }

            snippet = snippet.trim();
            char s = ' ';
            if (!snippet.isBlank()){
                s = snippet.charAt(snippet.length() - 1);
            }
            if (s != '.'){
                snippet = snippet + "...";
            }

            float rank = 0;
            float allRank = 0;

            for (IndexEntity indexEntity1 : indexEntitiesList){
                if(indexEntity.getLemmaId().getId() == indexEntity1.getLemmaId().getId()){
                    rank += indexEntity1.getRank();
                }
                allRank += indexEntity1.getRank();

            }

            float relevance = rank / allRank;
            PageSettings pageSettings = new PageSettings();
            pageSettings.setRelevance(relevance);
            pageSettings.setUri(indexEntity.getPageId().getPath());
            pageSettings.setTitle(title);
            pageSettings.setSite(siteEntity.getUrl());
            pageSettings.setSnippet(snippet);
            pageSettings.setSiteName(siteEntity.getName());
            data.add(pageSettings);


        }

        data.sort(Comparator.comparing(d -> d.relevance, Comparator.reverseOrder()));

        SearchSettings searchSettings = new SearchSettings();

        searchSettings.setResult(true);
        searchSettings.setData(data);
        searchSettings.setCount(indexEntitiesList.size());



        return searchSettings;
    }

    private List<LemmaEntity> searchLemmas(List<String> baseFormLemmas, SiteEntity siteEntity) {

        List<LemmaEntity> finalLemmaEntityList = new ArrayList<>();
        float pageCount = siteEntity == null ? pageRepository.count() : pageRepository.countBySiteId(siteEntity);
        for (String lemma : baseFormLemmas) {

            List<LemmaEntity> lemmaEntityList = new ArrayList<>();

            if (siteEntity == null)
                lemmaEntityList.addAll(lemmaRepository.findAllByLemma(lemma));
            else
                lemmaEntityList.add(lemmaRepository.findByLemmaAndSiteId(lemma, siteEntity.getId()));


            if (lemmaEntityList.size() == 0)
                continue;

            for (LemmaEntity lemmaEntity : lemmaEntityList) {
                float lemmaCount = lemmaEntity.getFrequency();
                float percent = (lemmaCount / pageCount) * 100;
                if (percent > 50)
                    continue;
                finalLemmaEntityList.add(lemmaEntity);
            }
        }
        return finalLemmaEntityList;
    }

    private List<IndexEntity> searchIndex(List<LemmaEntity> lemmaEntityList){
        List<IndexEntity> indexEntitiesList =indexRepository.findAllByLemmaId(lemmaEntityList.get(0));

        for (int i = 0; i < indexEntitiesList.size(); i++){
            for (int j = 1; j < lemmaEntityList.size(); j++){
                IndexEntity indexEntity = indexRepository.findByLemmaIdAndPageId(lemmaEntityList.get(j), indexEntitiesList.get(i).getPageId());
                if (indexEntity != null){
                    continue;
                }
                indexEntitiesList.remove(i);
                i--;
            }
        }
        return indexEntitiesList;
    }
}
