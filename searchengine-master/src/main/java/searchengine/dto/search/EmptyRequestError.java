package searchengine.dto.search;

import lombok.Getter;
import searchengine.repository.SearchRepository;

@Getter
public class EmptyRequestError implements SearchRepository {

    final boolean result = false;
    final String error = "Задан пустой поисковый запрос";
}
