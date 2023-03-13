package searchengine.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.springframework.stereotype.Service;
import searchengine.model.IndexEntity;
import searchengine.model.LemmaEntity;
import searchengine.model.PageEntity;
import searchengine.model.SiteEntity;
import searchengine.repository.IndexRepository;
import searchengine.repository.LemmaRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LemmaServiceImpl implements LemmaService {

    private final LemmaRepository lemmaRepository;
    private final IndexRepository indexRepository;

    @Override
    public synchronized void addLemma(Connection.Response document,
                                      SiteEntity site,
                                      PageEntity pageEntity) throws IOException {
        if (document == null){
            return;
        }
        SearchLemmas searchLemmas1 = SearchLemmas.getLuceneMorphology();
        Map<String, Integer> stringIntegerMap = searchLemmas1.lemma(document.parse());
        for (Map.Entry<String, Integer> lemma : stringIntegerMap.entrySet()) {
            LemmaEntity lemmaEntity = lemmaRepository.findByLemmaAndSiteId(lemma.getKey(), site.getId());
            if (lemmaEntity == null) {
                lemmaEntity = LemmaEntity.builder()
                        .site(site)
                        .lemma(lemma.getKey())
                        .frequency(1)
                        .build();
            } else {
                lemmaEntity.setFrequency(lemmaEntity.getFrequency() + 1);
            }
            lemmaRepository.save(lemmaEntity);
            Optional<IndexEntity> indexEntity = indexRepository.findByLemmaIdAndPageId(lemmaEntity, pageEntity);
            if (indexEntity.isEmpty()) {
                indexEntity = Optional.ofNullable(IndexEntity.builder()
                        .lemmaId(lemmaEntity)
                        .pageId(pageEntity)
                        .rank(lemma.getValue())
                        .build());
            } else {
                indexEntity.get().setRank(indexEntity.get().getRank() + 1);
            }
            indexRepository.save(indexEntity.get());
        }
    }

    public boolean deleteLemma(PageEntity pageEntity) {
        List<IndexEntity> indexList = indexRepository.findAllByPageId(pageEntity);
        if (indexList.isEmpty())
            return false;
        indexRepository.deleteAllByPageId(pageEntity);
        indexList.forEach(index -> {
            if (index.getLemmaId().getFrequency() > 1) {
                index.getLemmaId().setFrequency(index.getLemmaId().getFrequency() - 1);
                indexRepository.save(index);
            } else {
                lemmaRepository.deleteById(index.getLemmaId().getId());
            }
        });
        return true;
    }
}
