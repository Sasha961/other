package searchengine.dto.search;

import lombok.Getter;

@Getter
public class SearchError implements SearchRepository {

    final boolean result = false;
    final String error = "Задан пустой поисковый запрос";
}
