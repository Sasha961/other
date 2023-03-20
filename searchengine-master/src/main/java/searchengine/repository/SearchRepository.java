package searchengine.repository;

import org.springframework.stereotype.Repository;
import searchengine.dto.search.EmptyRequestError;
import searchengine.dto.search.SearchLemmaError;
import searchengine.dto.search.SearchSettings;

@Repository
public interface SearchRepository {

    SearchSettings searchSettings = new SearchSettings();

    EmptyRequestError emptyRequestError = new EmptyRequestError();

    SearchLemmaError searchLemmaError = new SearchLemmaError();
}
