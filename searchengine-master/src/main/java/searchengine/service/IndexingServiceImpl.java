package searchengine.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import searchengine.components.BaseSettings;
import searchengine.config.Site;
import searchengine.config.SitesList;
import searchengine.dto.indexing.*;
import searchengine.model.PageEntity;
import searchengine.model.SiteEntity;
import searchengine.repository.IndexingRepository;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class IndexingServiceImpl implements IndexingService {

    private final SitesList sitesList;
    private final LemmaService lemmaService;
    private final BaseSettings baseSettings;
    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;
    private static ForkJoinPool forkJoinPool = new ForkJoinPool();
    private final List<SiteEntity> SiteEntitiesList = new ArrayList<>();
    private EnumerationOfLinks enumerationOfLinks;

    private static final String LAST_ERROR_MESSAGE = "Остановлено пользователем";

    @Override
    public IndexingRepository fullIndexingPages() {
        if (forkJoinPool.getPoolSize() != 0)
            return new FullIndexingError();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (Site sites : sitesList.getSites()) {
            if (siteRepository.findByUrl(sites.getUrl()).isPresent())
                baseSettings.deleteAllBySite(sites.getUrl());
            SiteEntity site = SiteEntity.builder()
                    .name(sites.getName())
                    .url(sites.getUrl())
                    .status(EnumStatusAtSite.INDEXING)
                    .statusTime(LocalDateTime.now()).build();
            siteRepository.save(site);
            SiteEntitiesList.add(site);
            executorService.submit(() -> {
                baseSettings.addToBase(sites.getUrl() + "/", site);
                enumerationOfLinks = new EnumerationOfLinks(site.getUrl(),
                        pageRepository, siteRepository, site, lemmaService, baseSettings);
                forkJoinPool.invoke(enumerationOfLinks);
                site.setStatus(EnumStatusAtSite.INDEXED);
                siteRepository.save(site);
            });
        }
        return new ResultTrue();
    }

    @Override
    public IndexingRepository stopIndexingPages() {
        if (forkJoinPool.getPoolSize() == 0)
            return new StopIndexingError();
//            enumerationOfLinks.doStop();
        SiteEntitiesList.stream()
                .filter(site -> site.getStatus() == EnumStatusAtSite.INDEXING)
                .forEach(site -> {
                    site.setStatus(EnumStatusAtSite.FAILED);
                    site.setLastError(LAST_ERROR_MESSAGE);
                    siteRepository.save(site);
                });
        try {
            forkJoinPool.shutdownNow();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        forkJoinPool = new ForkJoinPool();
        return new ResultTrue();
    }

    @Override
    public IndexingRepository indexingPage(String url) {
        for (Site site : sitesList.getSites()) {
            if (url.contains(site.getName().toLowerCase())) {
                Optional<PageEntity> pageEntity = pageRepository.findByPath(url.replace(site.getUrl(), ""));
                Optional<SiteEntity> siteEntity = siteRepository.findByUrl(site.getUrl());
                pageEntity.ifPresent(baseSettings::deletePage);
                baseSettings.addToBase(url, siteEntity.get());
                return new ResultTrue();
            }
        }
        return new IndexingPageError();
    }
}
