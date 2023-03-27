package searchengine.service;

import org.jsoup.nodes.Document;
import searchengine.model.Page;
import searchengine.model.Site;

import java.io.IOException;

public interface LemmaService{

    void addLemma(Document document, Site site, Page pageEntity) throws IOException;

    boolean deleteLemma(Page pageEntity);
}
