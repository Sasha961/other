package searchengine.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import searchengine.components.BaseSettings;
import searchengine.config.Config;
import searchengine.model.Site;
import searchengine.repository.PageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@RequiredArgsConstructor
public class EnumerationOfLinks extends RecursiveAction {
    private final String link;
    private final PageRepository pageRepository;
    private final Site site;
    private final BaseSettings baseSettings;
    private final Config config;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock writeLock = readWriteLock.writeLock();
    public volatile boolean isStop = false;


    @Override
    protected void compute() {
            if (checkLink(link,site)) return;
            TreeSet<String> allLinks = new TreeSet<>();
            try {
                Connection.Response connection = Jsoup.connect(link)
                        .ignoreHttpErrors(true)
                        .userAgent(config.getUserAgent())
                        .referrer(config.getUserAgent())
                        .execute();
                Thread.sleep(300);
                if (!connection.contentType().startsWith("text/html")) return;
                Elements links = connection.parse().select("a");
                List<EnumerationOfLinks> linksJoin = new ArrayList<>();
                links.stream()
                        .map(el -> el.attr("abs:href"))
                        .filter(link -> checkLink(link, site))
                        .forEach(link -> {
                            allLinks.add(link);
                            writeLock.lock();
                            baseSettings.addToBase(link, site);
                            writeLock.unlock();
                        });
                allLinks.forEach(link -> {
                    EnumerationOfLinks enumerationOfLinks = new EnumerationOfLinks(link,
                            pageRepository,
                            site,
                            baseSettings,
                            config);
                    enumerationOfLinks.fork();
                    linksJoin.add(enumerationOfLinks);
                });
                if (isStop){
                    linksJoin.clear();
                    return;
                }
                if (!linksJoin.isEmpty()) linksJoin.forEach(ForkJoinTask::invokeAll);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private boolean checkLink(String link, Site site) {
        writeLock.lock();
        boolean result = link.
                startsWith(site.getUrl()) && (!link.endsWith("#")
                && !link.endsWith("png")
                && !link.endsWith("jpg")
                && !link.endsWith("JPG")
                && !link.endsWith("PNG"))
                && !link.endsWith("pdf")
                && !link.endsWith("PDF") &&
                !link.equals(this.link) &&
                pageRepository.findByPathAndSiteId(link.replaceAll(site.getUrl(), ""), site).isEmpty();
        writeLock.unlock();
        return result;
    }
}
