package searchengine.dto.Responce.search;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import searchengine.repository.SearchRepository;

@Getter
public class EmptyRequestError implements SearchRepository {

    final boolean result = false;
    final String error = "Задан пустой поисковый запрос";
}
