package searchengine.dto.indexing;

import lombok.Getter;

@Getter
public class FullIndexingError implements IndexingRepository {

    final boolean result = false;
    final String error = "Индексация уже запущена";
}
