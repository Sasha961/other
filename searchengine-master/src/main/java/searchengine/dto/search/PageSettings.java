package searchengine.dto.search;

import lombok.Data;

@Data
public class PageSettings {

    public String site;
    public String siteName;
    public String uri;
    public String title;
    public String snippet;
    public float relevance;

}
