package searchengine.dto.responce.search;

import lombok.Data;
import searchengine.repository.SearchRepository;

import java.util.List;

@Data
public class SearchSettings implements SearchRepository {
    boolean result;
    long count;
    List<SearchPageSettings> data;
}
