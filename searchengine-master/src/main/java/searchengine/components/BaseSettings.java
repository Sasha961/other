package searchengine.components;

import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.springframework.stereotype.Component;
import searchengine.config.Connect;
import searchengine.model.PageEntity;
import searchengine.model.SiteEntity;
import searchengine.repository.IndexRepository;
import searchengine.repository.LemmaRepository;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;
import searchengine.service.LemmaService;

import java.time.LocalDateTime;
import java.util.List;

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


            Connection.Response connection = new Connect().getDocumentConnect(link);
            site.setStatusTime(LocalDateTime.now());
            siteRepository.save(site);
            PageEntity pageEntity = PageEntity.builder()
                    .code(connection.statusCode())
                    .siteId(site)
                    .path(link.replaceAll(site.getUrl(), "/"))
                    .content(connection.body())
                    .build();
            pageRepository.save(pageEntity);
            lemmaService.addLemma(connection, site, pageEntity);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void deleteAllBySite(String site) {
        SiteEntity siteId = siteRepository.findByUrl(site);
        List<PageEntity> pages = pageRepository.findAllBySiteId(siteId);
        pages.forEach(indexRepository::deleteAllByPageId);
        lemmaRepository.deleteAllBySiteId(siteId.getId());
        siteRepository.deleteByUrl(site);
    }

    public void deletePage(PageEntity pageEntity) {
        indexRepository.deleteAllByPageId(pageEntity);
        lemmaService.deleteLemma(pageEntity);
        pageRepository.deleteByPath(pageEntity.getPath());
    }
}
