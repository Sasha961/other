package searchengine.dto;

import lombok.Data;
import searchengine.model.Page;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageAndRank {
    List<Page> pageList = new ArrayList<>();

    public Page page;

    public float rank;
}
