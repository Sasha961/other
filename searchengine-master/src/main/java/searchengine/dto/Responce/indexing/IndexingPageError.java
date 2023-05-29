package searchengine.dto.responce.indexing;

import lombok.Getter;
import searchengine.repository.IndexingRepository;

@Getter
public class IndexingPageError implements IndexingRepository {

    final boolean result = false;
    final String error = "Указанная страница не найдена";
}
