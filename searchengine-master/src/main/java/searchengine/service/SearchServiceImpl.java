package searchengine.service;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import searchengine.config.SitesList;
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
    public  Map<String, ?> search(String query, String site, int offset, int limit) {

        Set<String> querySplit = new HashSet<>(Arrays.asList(query.split(" +")));
        SearchLemmas searchLemmas = SearchLemmas.getLuceneMorphology();
        List<java.lang.String> baseFormLemmas = new ArrayList<>();
        List<LemmaEntity> lemmaEntityList = new ArrayList<>();

        for (String s : querySplit){
            if (searchLemmas.baseFormLemma(s.toLowerCase()) != null){
                baseFormLemmas.add(searchLemmas.baseFormLemma(s.toLowerCase(Locale.ROOT)));
            }
        }


        for (String lemma : baseFormLemmas){

            SiteEntity site1 = siteRepository.findByUrl(site);

            LemmaEntity lemmaEntity = lemmaRepository.findByLemmaAndSiteId(lemma, site1.getId());

            if (lemmaEntity == null){
                continue;
            }

            float pageCount = pageRepository.count();
            float lemmaCount = lemmaEntity.getFrequency();

            float percent = (lemmaCount / pageCount) * 100;

            if (percent > 50) {
                continue;
            }
            lemmaEntityList.add(lemmaEntity);
        }
        if (lemmaEntityList.size() == 0){
            return Map.of("result","нет совпадений" );
        }

       lemmaEntityList.sort(Comparator.comparing(m -> m.getFrequency()));

        List<IndexEntity> indexEntitiesList = new ArrayList<>();


        indexEntitiesList = indexRepository.findAllByLemmaId(lemmaEntityList.get(0));


        for (int i = 1; i < lemmaEntityList.size(); i++) {
            for (int j = 0; j< indexEntitiesList.size() ; j++) {
                if (lemmaEntityList.get(i).getId() == indexEntitiesList.get(j).getLemmaId().getId()) {
                    continue;
                }
                indexEntitiesList.remove(j);
            }
        }


        for (IndexEntity indexEntity : indexEntitiesList){

            Optional<PageEntity> pageEntity = pageRepository.findById(indexEntity.getPageId().getId());

            String elements = pageEntity.get().getContent();

            int titleBegin = elements.indexOf("<title>") + 7;
            int titleEnd = elements.indexOf("</title>");

            String title = elements.substring(titleBegin, titleEnd);

            int snippetPosition = elements.indexOf(indexEntity.getLemmaId().getLemma());

            String[] clearContent = elements.replaceAll("[^А-я]", " ").toLowerCase().split(" +");



            System.out.println(title);

            Arrays.stream(clearContent).forEach(System.out::println);


            System.exit(1);

        }





        System.exit(1);

        return null;
    }
}
