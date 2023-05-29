package searchengine.repository;

import org.springframework.stereotype.Repository;
import searchengine.dto.responce.indexing.FullIndexingError;
import searchengine.dto.responce.indexing.IndexingPageError;
import searchengine.dto.responce.indexing.ResultTrue;
import searchengine.dto.responce.indexing.StopIndexingError;

@Repository
public interface IndexingRepository {
    ResultTrue resultTrue = new ResultTrue();
    FullIndexingError fullIndexingError = new FullIndexingError();
    StopIndexingError stopIndexingError = new StopIndexingError();
    IndexingPageError indexingPageError = new IndexingPageError();
}
