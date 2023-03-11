package searchengine.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.SiteEntity;

import javax.transaction.Transactional;

@Repository
public interface SiteRepository extends CrudRepository<SiteEntity, Integer> {

    SiteEntity findByUrl(String url);
    @Transactional
    void deleteByUrl(String url);
}
