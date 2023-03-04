package searchengine.service;

import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.select.Elements;
import searchengine.config.Connect;
import searchengine.components.BaseSettings;
import searchengine.model.SiteEntity;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

@RequiredArgsConstructor
public class ForkJoinPoolLinks extends RecursiveAction {

    private final String link;
    private final PageRepository pageRepository;
    private final SiteRepository siteRepository;
    private final SiteEntity site;
    private final LemmaService lemmaService;
    private final BaseSettings addToBase;

    @Override
    protected void compute() {
        TreeSet<String> allLinks = new TreeSet<>();
        try {
            Connection.Response connection = new Connect().getDocumentConnect(link);
            Thread.sleep(300);
            if (!connection.contentType().startsWith("text/html"))
                return;
            Elements links = connection.parse().select("a");
            List<ForkJoinPoolLinks> linksJoin = new ArrayList<>();
            links.stream()
                    .map(el -> el.attr("abs:href"))
                    .filter(link -> checkLink(link, site))
                    .forEach(link -> {
                        addToBase.addToBase(link, site);
                        allLinks.add(link);
                    });
            allLinks.forEach(link -> {
                ForkJoinPoolLinks forkJoinPoolService = new ForkJoinPoolLinks(link,
                        pageRepository,
                        siteRepository,
                        site,
                        lemmaService,
                        addToBase);
                forkJoinPoolService.fork();
                linksJoin.add(forkJoinPoolService);
            });
            if (!linksJoin.isEmpty())
                linksJoin.forEach(ForkJoinTask::invokeAll);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private synchronized boolean checkLink(String link, SiteEntity site) {
        return link.startsWith(site.getUrl()) && !link.endsWith("#|.jpg|.png") && !link.equals(this.link) &&
                pageRepository.countByPathAndSiteId(link.replaceAll(site.getUrl(), "/"), site) == 0;
    }
}
