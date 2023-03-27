package searchengine.dto.Responce.indexing;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import searchengine.repository.IndexingRepository;

@Getter
public class StopIndexingError implements IndexingRepository {

    final boolean result = false;
    final String error = "Индексация не запущена";
}
