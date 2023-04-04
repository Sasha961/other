package searchengine.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.Site;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface SiteRepository extends CrudRepository<Site, Integer> {

   Optional<Site> findByUrl(String url);
    @Transactional
    void deleteByUrl(String url);
}
