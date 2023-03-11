package searchengine.service;


import searchengine.dto.indexing.IndexingRepository;

public interface IndexingService {
    IndexingRepository fullIndexingPages();

    IndexingRepository stopIndexingPages();

    IndexingRepository indexingPage(String url);

}
