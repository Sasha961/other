package searchengine.service;


import java.util.Map;

public interface SearchService {

    Map<String, ?> search(String query, String site, int offset, int limit);
}
