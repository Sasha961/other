package searchengine.service;


import org.springframework.http.ResponseEntity;
import searchengine.repository.IndexingRepository;

public interface IndexingService {
    ResponseEntity<IndexingRepository> fullIndexingPages();

    ResponseEntity<IndexingRepository> stopIndexingPages();

    ResponseEntity<IndexingRepository> indexingPage(String url);

}
