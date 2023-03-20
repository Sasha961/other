package searchengine.service;


import searchengine.repository.IndexingRepository;

public interface IndexingService {
    IndexingRepository fullIndexingPages();

    IndexingRepository stopIndexingPages();

    IndexingRepository indexingPage(String url);

}
