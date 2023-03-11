package searchengine.dto.indexing;

import lombok.Getter;

@Getter
public class IndexingPageError implements IndexingRepository {

    final boolean result = false;
    final String error = "Запрашиваемая страница не найдена";
}
