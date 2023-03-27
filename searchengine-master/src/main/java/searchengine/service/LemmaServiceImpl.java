package searchengine.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import searchengine.model.Index;
import searchengine.model.Lemma;
import searchengine.model.Page;
import searchengine.model.Site;
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
    public synchronized void addLemma(Document document,
                                      Site site,
                                      Page pageEntity) throws IOException {

        SearchLemmas searchLemmas1 = SearchLemmas.getLuceneMorphology();
        Map<String, Integer> lemmaParse = searchLemmas1.lemma(document);
        for (Map.Entry<String, Integer> lemma : lemmaParse.entrySet()) {
            Lemma lemmaEntity = lemmaRepository.findByLemmaAndSiteId(lemma.getKey(), site.getId());
            if (lemmaEntity == null) {
                lemmaEntity = Lemma.builder()
                        .site(site)
                        .lemma(lemma.getKey())
                        .frequency(1)
                        .build();
            } else {
                lemmaEntity.setFrequency(lemmaEntity.getFrequency() + 1);
            }
            lemmaRepository.save(lemmaEntity);
            Optional<Index> indexEntity = indexRepository.findByLemmaIdAndPageId(lemmaEntity, pageEntity);
            if (indexEntity.isEmpty()) {
                indexEntity = Optional.ofNullable(Index.builder()
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

    public boolean deleteLemma(Page pageEntity) {
        List<Index> indexList = indexRepository.findAllByPageId(pageEntity);
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
