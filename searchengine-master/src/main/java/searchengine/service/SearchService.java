package searchengine.service;


import searchengine.dto.search.SearchRepository;

public interface SearchService {

    SearchRepository search(String query, String site, int offset, int limit);
}
