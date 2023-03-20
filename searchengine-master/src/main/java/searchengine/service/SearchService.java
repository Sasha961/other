package searchengine.service;


import searchengine.repository.SearchRepository;

import java.util.Optional;

public interface SearchService {

    SearchRepository search(String query, Optional<String> site, int offset, int limit);
}
