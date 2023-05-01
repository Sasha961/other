package searchengine.service;


import org.springframework.http.ResponseEntity;
import searchengine.repository.SearchRepository;

import java.util.Optional;

public interface SearchService {


    ResponseEntity<SearchRepository> search(String query, Optional<String> site, int offset, int limit);
}
