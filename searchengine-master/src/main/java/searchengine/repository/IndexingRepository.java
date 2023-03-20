package searchengine.repository;

import org.springframework.stereotype.Repository;
import searchengine.dto.indexing.FullIndexingError;
import searchengine.dto.indexing.IndexingPageError;
import searchengine.dto.indexing.ResultTrue;
import searchengine.dto.indexing.StopIndexingError;

@Repository
public interface IndexingRepository {

    ResultTrue resultTrue = new ResultTrue();
    FullIndexingError fullIndexingError = new FullIndexingError();
    StopIndexingError stopIndexingError = new StopIndexingError();
    IndexingPageError indexingPageError = new IndexingPageError();
}
