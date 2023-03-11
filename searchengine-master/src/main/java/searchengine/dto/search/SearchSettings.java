package searchengine.dto.search;

import lombok.Data;

import java.util.List;

@Data
public class SearchSettings implements SearchRepository {

    boolean result;
    long count;
    List<PageSettings> data;
}
