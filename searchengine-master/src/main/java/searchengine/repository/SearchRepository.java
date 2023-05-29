package searchengine.repository;

import org.springframework.stereotype.Repository;
import searchengine.dto.responce.search.EmptyRequestError;
import searchengine.dto.responce.search.SearchLemmaError;
import searchengine.dto.responce.search.SearchSettings;

@Repository
public interface SearchRepository{
    SearchSettings searchSettings = new SearchSettings();

    EmptyRequestError emptyRequestError = new EmptyRequestError();

    SearchLemmaError searchLemmaError = new SearchLemmaError();
}
