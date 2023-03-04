package searchengine.service;

import org.jsoup.Connection;
import searchengine.model.PageEntity;
import searchengine.model.SiteEntity;

import java.io.IOException;

public interface LemmaService{

    void addLemma(Connection.Response document, SiteEntity site, PageEntity pageEntity) throws IOException;

    boolean deleteLemma(PageEntity pageEntity);
}
