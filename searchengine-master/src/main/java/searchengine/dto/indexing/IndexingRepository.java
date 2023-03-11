package searchengine.dto.indexing;

public interface IndexingRepository {

    ResultTrue resultTrue = new ResultTrue();
    FullIndexingError indexingError = new FullIndexingError();
    StopIndexingError stopIndexingError = new StopIndexingError();
    IndexingPageError indexingPageError = new IndexingPageError();
}
