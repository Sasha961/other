package searchengine.dto.responce.search;

import lombok.Getter;
import searchengine.repository.SearchRepository;

@Getter
public class SearchLemmaError implements SearchRepository {
    final boolean result = false;

    final String error = "Ни одно слово, не встречается на страницах";
}
