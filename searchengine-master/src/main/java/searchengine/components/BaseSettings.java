package searchengine.components;

import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.springframework.stereotype.Component;
import searchengine.model.PageEntity;
import searchengine.model.SiteEntity;
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

    public synchronized void addToBase(String link, SiteEntity site) {
        try {
            if (pageRepository.findByPathAndSiteId(link.replaceAll(site.getUrl(), ""), site).isPresent()){
                return;
            }
            Connection.Response connection = Connect.getDocumentConnect(link);
            if (connection.parse() == null) {
                return;
            }
            site.setStatusTime(LocalDateTime.now());
            siteRepository.save(site);
            PageEntity pageEntity = PageEntity.builder()
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
        Optional<SiteEntity> siteId = siteRepository.findByUrl(site);
        List<PageEntity> pages = pageRepository.findAllBySiteId(siteId.get());
        pages.forEach(indexRepository::deleteAllByPageId);
        lemmaRepository.deleteAllBySiteId(siteId.get().getId());
        siteRepository.deleteByUrl(site);
    }

    public void deletePage(PageEntity pageEntity) {
        indexRepository.deleteAllByPageId(pageEntity);
        lemmaService.deleteLemma(pageEntity);
        pageRepository.deleteByPath(pageEntity.getPath());
    }
}
