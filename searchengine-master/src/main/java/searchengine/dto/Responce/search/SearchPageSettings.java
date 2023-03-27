package searchengine.dto.Responce.search;

import lombok.Data;

@Data
public class SearchPageSettings {

    public String site;
    public String siteName;
    public String uri;
    public String title;
    public String snippet;
    public float relevance;

}
