package searchengine.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import searchengine.components.BaseSettings;
import searchengine.config.Config;
import searchengine.config.SitesList;
import searchengine.dto.EnumStatusAtSite;
import searchengine.dto.Responce.indexing.FullIndexingError;
import searchengine.dto.Responce.indexing.IndexingPageError;
import searchengine.dto.Responce.indexing.ResultTrue;
import searchengine.dto.Responce.indexing.StopIndexingError;
import searchengine.model.Page;
import searchengine.model.Site;
import searchengine.repository.IndexingRepository;
import searchengine.repository.PageRepository;
import searchengine.repository.SiteRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
@RequiredArgsConstructor
public class IndexingServiceImpl implements IndexingService {

    private final SitesList sitesList;
    private final BaseSettings baseSettings;
    private final SiteRepository siteRepository;
    private final PageRepository pageRepository;
    private static ForkJoinPool forkJoinPool = new ForkJoinPool();
    private final List<Site> SiteEntitiesList = new ArrayList<>();
    private EnumerationOfLinks enumerationOfLinks;
    private final Config config;
    private static final String LAST_ERROR_MESSAGE = "Остановлено пользователем";
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock writeLock = readWriteLock.writeLock();
    private ExecutorService executorService;

    @Override
    public ResponseEntity<IndexingRepository> fullIndexingPages() {
        if (forkJoinPool.getPoolSize() != 0)
            return new ResponseEntity<>(new FullIndexingError(), HttpStatus.BAD_REQUEST);
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        for (searchengine.config.Site sites : sitesList.getSites()) {
            if (siteRepository.findByUrl(sites.getUrl()).isPresent())
                baseSettings.deleteAllBySite(sites.getUrl());
            Site site = Site.builder()
                    .name(sites.getName())
                    .url(sites.getUrl())
                    .status(EnumStatusAtSite.INDEXING)
                    .statusTime(LocalDateTime.now()).build();
            siteRepository.save(site);
            SiteEntitiesList.add(site);
            executorService.submit(() -> {
                writeLock.lock();
                baseSettings.addToBase(sites.getUrl() + "/", site);
                writeLock.unlock();
                enumerationOfLinks = new EnumerationOfLinks(site.getUrl(),
                        pageRepository,
                        site,
                        baseSettings,
                        config);
                forkJoinPool.invoke(enumerationOfLinks);
                site.setStatus(EnumStatusAtSite.INDEXED);
                siteRepository.save(site);
            });
        }
        return new ResponseEntity<>(new ResultTrue(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<IndexingRepository> stopIndexingPages() {
        if (forkJoinPool.getPoolSize() == 0)
            return new ResponseEntity<>(new StopIndexingError(), HttpStatus.BAD_REQUEST);
        SiteEntitiesList.stream()
                .filter(site -> site.getStatus() == EnumStatusAtSite.INDEXING)
                .forEach(site -> {
                    site.setStatus(EnumStatusAtSite.FAILED);
                    site.setLastError(LAST_ERROR_MESSAGE);
                    siteRepository.save(site);
                });
        try {
            while (!forkJoinPool.awaitTermination(1, TimeUnit.SECONDS) ||
                    !executorService.awaitTermination(1, TimeUnit.SECONDS)){
                forkJoinPool.shutdownNow();
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        forkJoinPool = new ForkJoinPool();
        return new ResponseEntity<>(new ResultTrue(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<IndexingRepository> indexingPage(String url) {
        for (searchengine.config.Site site : sitesList.getSites()) {
            if (url.toLowerCase().contains(site.getUrl().toLowerCase()) && url.toLowerCase().contains("http")) {
                Optional<Page> pageEntity = pageRepository.findByPath(url.replace(site.getUrl(), ""));
                Optional<Site> siteEntity = siteRepository.findByUrl(site.getUrl());
                writeLock.lock();
                pageEntity.ifPresent(baseSettings::deletePage);
                baseSettings.addToBase(url, siteEntity.get());
                writeLock.unlock();
                return new ResponseEntity<>(new ResultTrue(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new IndexingPageError(), HttpStatus.BAD_REQUEST);
    }
}
