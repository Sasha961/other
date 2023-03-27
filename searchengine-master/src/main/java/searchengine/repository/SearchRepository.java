package searchengine.repository;

import org.springframework.stereotype.Repository;
import searchengine.dto.Responce.search.EmptyRequestError;
import searchengine.dto.Responce.search.SearchLemmaError;
import searchengine.dto.Responce.search.SearchSettings;

@Repository
public interface SearchRepository {

    SearchSettings searchSettings = new SearchSettings();

    EmptyRequestError emptyRequestError = new EmptyRequestError();

    SearchLemmaError searchLemmaError = new SearchLemmaError();
}
