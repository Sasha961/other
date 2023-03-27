package searchengine.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.Index;
import searchengine.model.Lemma;
import searchengine.model.Page;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface IndexRepository extends CrudRepository<Index, Integer> {

    List<Index> findAllByPageId(Page pageEntity);
    @Transactional
    void deleteAllByPageId(Page pageEntity);
    Optional<Index> findByLemmaIdAndPageId(Lemma lemmaEntity, Page pageEntity);
    List<Index> findAllByLemmaId(Lemma lemmaEntity);
}
