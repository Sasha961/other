package searchengine.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.Page;
import searchengine.model.Site;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface PageRepository extends CrudRepository<Page, Integer> {
    @Transactional
    Optional<Page> findByPathAndSiteId(String path, Site site);
    Optional<Page> findByPath(String path);
    @Transactional
    void deleteByPath(String path);
    List<Page> findAllBySiteId(Site id);
    int countBySiteId(Site id);
}
