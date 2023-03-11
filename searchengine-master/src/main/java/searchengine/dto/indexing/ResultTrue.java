package searchengine.dto.indexing;

import lombok.Getter;

@Getter
public class ResultTrue implements IndexingRepository{

    final boolean result = true;
}
