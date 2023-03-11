package searchengine.dto.indexing;

import lombok.Getter;

@Getter
public class StopIndexingError implements IndexingRepository{

    final boolean result = false;
    final String error = "Индексация не запущена";
}
