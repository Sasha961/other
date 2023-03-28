package searchengine.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import searchengine.components.BaseSettings;
import searchengine.config.Config;
import searchengine.model.Site;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

@RequiredArgsConstructor
public class EnumerationOfLinks extends RecursiveAction {

    private final String link;
    private final PageRepository pageRepository;
    private final SiteRepository siteRepository;
    private final Site site;
    private final LemmaService lemmaService;
    private final BaseSettings addToBase;
    private final Config config;

    @Override
    protected void compute() {
            if (checkLink(link,site)){
                return;
            }
            TreeSet<String> allLinks = new TreeSet<>();
            try {
                Connection.Response connection = Jsoup.connect(link)
                        .ignoreHttpErrors(true)
                        .userAgent(config.getUserAgent())
                        .referrer(config.getUserAgent())
                        .execute();
                Thread.sleep(300);
                if (!connection.contentType().startsWith("text/html"))
                    return;
                Elements links = connection.parse().select("a");
                List<EnumerationOfLinks> linksJoin = new ArrayList<>();
                links.stream()
                        .map(el -> el.attr("abs:href"))
                        .filter(link -> checkLink(link, site))
                        .forEach(link -> {
                            allLinks.add(link);
                            addToBase.addToBase(link, site);
                        });
                allLinks.forEach(link -> {
                    EnumerationOfLinks enumerationOfLinks = new EnumerationOfLinks(link,
                            pageRepository,
                            siteRepository,
                            site,
                            lemmaService,
                            addToBase,
                            config);
                    enumerationOfLinks.fork();
                    linksJoin.add(enumerationOfLinks);
                });
                if (!linksJoin.isEmpty())
                    linksJoin.forEach(ForkJoinTask::invokeAll);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private synchronized boolean checkLink(String link, Site site) {
        return link.startsWith(site.getUrl()) &&
                (!link.endsWith("#") &&
                !link.endsWith("png") &&
                !link.endsWith("jpg") &&
                !link.endsWith("JPG") &&
                !link.endsWith("PNG")) && !link.equals(this.link) &&
                pageRepository.findByPathAndSiteId(link.replaceAll(site.getUrl(), ""), site).isEmpty();
    }
}
