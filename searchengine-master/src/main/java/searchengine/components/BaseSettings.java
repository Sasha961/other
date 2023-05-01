package searchengine.components;

import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;
import searchengine.config.Config;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.repository.IndexRepository;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;
import searchengine.service.LemmaService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BaseSettings {
    final PageRepository pageRepository;
    final SiteRepository siteRepository;
    final LemmaService lemmaService;
    final IndexRepository indexRepository;
    final LemmaRepository lemmaRepository;
    final Config config;

    public void addToBase(String link, Site site) {
        try {
            if (pageRepository.findByPathAndSiteId(link.replaceAll(site.getUrl(), ""), site).isPresent()){
                return;
            }
            Connection.Response connection = Jsoup.connect(link)
                    .ignoreHttpErrors(true)
                    .userAgent(config.getUserAgent())
                    .referrer(config.getReferrer())
                    .execute();
            if (connection.parse() == null) {
                return;
            }
            site.setStatusTime(LocalDateTime.now());
            siteRepository.save(site);
            Page pageEntity = Page.builder()
                    .code(connection.statusCode())
                    .siteId(site)
                    .path(link.replaceAll(site.getUrl(), ""))
                    .content(connection.body())
                    .build();
            pageRepository.save(pageEntity);
            lemmaService.addLemma(connection.parse(), site, pageEntity);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void deleteAllBySite(String site) {
        Optional<Site> siteId = siteRepository.findByUrl(site);
        List<Page> pages = pageRepository.findAllBySiteId(siteId.get());
        pages.forEach(indexRepository::deleteAllByPageId);
        lemmaRepository.deleteAllBySiteId(siteId.get().getId());
        siteRepository.deleteByUrl(site);
    }

    public void deletePage(Page pageEntity) {
        indexRepository.deleteAllByPageId(pageEntity);
        lemmaService.deleteLemma(pageEntity);
        pageRepository.deleteByPath(pageEntity.getPath());
    }
}
