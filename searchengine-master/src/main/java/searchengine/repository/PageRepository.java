package searchengine.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.PageEntity;
import searchengine.model.SiteEntity;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface PageRepository extends CrudRepository<PageEntity, Integer> {

    @Transactional
    int countByPathAndSiteId(String path, SiteEntity site);

    PageEntity findByPath(String path);

    @Transactional
    void deleteByPath(String path);

     @Transactional
     @Modifying
     @Query(value = "alter table page alter index pathId visible", nativeQuery = true)
     void addIndex();

     @Transactional
     @Modifying
     @Query(value = "alter table page alter index pathId invisible", nativeQuery = true)
     void deleteIndex();

    List<PageEntity> findAllBySiteId(SiteEntity id);

    int countBySiteId(SiteEntity id);
}
