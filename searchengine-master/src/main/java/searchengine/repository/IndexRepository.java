package searchengine.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.IndexEntity;
import searchengine.model.LemmaEntity;
import searchengine.model.PageEntity;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface IndexRepository extends CrudRepository<IndexEntity, Integer> {

    List<IndexEntity> findAllByPageId(PageEntity pageEntity);
    @Transactional
    void deleteAllByPageId(PageEntity pageEntity);
    IndexEntity findByLemmaIdAndPageId(LemmaEntity lemmaEntity, PageEntity pageEntity);
    List<IndexEntity> findAllByLemmaId(LemmaEntity lemmaEntity);
}
