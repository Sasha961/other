package searchengine.repository;

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
    List<PageEntity> findAllBySiteId(SiteEntity id);
    int countBySiteId(SiteEntity id);
}
