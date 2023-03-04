package searchengine.service;


import java.util.Map;

public interface IndexingService {
    Map<String, ?> fullIndexingPages();

    Map<String, ?> stopIndexingPages();

    Map<String, ?> indexingPage(String url);
}
