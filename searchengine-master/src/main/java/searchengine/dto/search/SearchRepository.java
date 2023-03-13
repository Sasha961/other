package searchengine.dto.search;

public interface SearchRepository {

    SearchSettings searchSettings = new SearchSettings();

    SearchError searchError = new SearchError();

    LemmaError lemmasError = new LemmaError();
}
