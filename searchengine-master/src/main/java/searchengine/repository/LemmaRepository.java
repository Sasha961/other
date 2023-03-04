package searchengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.LemmaEntity;

import javax.transaction.Transactional;

@Repository
public interface LemmaRepository extends JpaRepository<LemmaEntity, Integer> {

    LemmaEntity findByLemmaAndSiteId(String lemma, int siteId);

    @Transactional
    void deleteAllBySiteId(int id);

    int countBySiteId(int id);

    int countByLemma(String lemma);

    LemmaEntity findByLemma(String lemma);
}
