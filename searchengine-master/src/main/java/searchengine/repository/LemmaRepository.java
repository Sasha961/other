package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.LemmaEntity;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface LemmaRepository extends JpaRepository<LemmaEntity, Integer> {

    LemmaEntity findByLemmaAndSiteId(String lemma, int siteId);
    @Transactional
    void deleteAllBySiteId(int id);
    int countBySiteId(int id);
    List<LemmaEntity> findAllByLemma(String lemma);


}
