package searchengine.service;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import searchengine.model.PageEntity;
import searchengine.model.SiteEntity;

import java.io.IOException;

public interface LemmaService{

    void addLemma(Document document, SiteEntity site, PageEntity pageEntity) throws IOException;

    boolean deleteLemma(PageEntity pageEntity);
}
