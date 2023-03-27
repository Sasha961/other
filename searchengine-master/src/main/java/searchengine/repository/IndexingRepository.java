package searchengine.repository;

import org.springframework.stereotype.Repository;
import searchengine.dto.Responce.indexing.FullIndexingError;
import searchengine.dto.Responce.indexing.IndexingPageError;
import searchengine.dto.Responce.indexing.ResultTrue;
import searchengine.dto.Responce.indexing.StopIndexingError;

@Repository
public interface IndexingRepository {

    ResultTrue resultTrue = new ResultTrue();
    FullIndexingError fullIndexingError = new FullIndexingError();
    StopIndexingError stopIndexingError = new StopIndexingError();
    IndexingPageError indexingPageError = new IndexingPageError();
}
