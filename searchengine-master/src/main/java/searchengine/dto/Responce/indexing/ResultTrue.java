package searchengine.dto.responce.indexing;

import lombok.Getter;
import searchengine.repository.IndexingRepository;

@Getter
public class ResultTrue implements IndexingRepository {
    final boolean result = true;
}
