package searchengine.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import searchengine.repository.IndexingRepository;
import searchengine.repository.SearchRepository;
import searchengine.dto.statistics.StatisticsResponse;
import searchengine.service.IndexingService;
import searchengine.service.SearchService;
import searchengine.service.StatisticsService;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final StatisticsService statisticsService;
    private final IndexingService indexingService;
    private final SearchService searchService;

    @GetMapping("/statistics")
    public ResponseEntity<StatisticsResponse> statistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }

    @GetMapping(value = "/startIndexing")
    public ResponseEntity<IndexingRepository> fullIndexing() {
        return ResponseEntity.ok(indexingService.fullIndexingPages());
    }

    @GetMapping("/stopIndexing")
    public ResponseEntity<IndexingRepository> stopIndexing() {
        return ResponseEntity.ok(indexingService.stopIndexingPages());
    }

    @PostMapping("/indexPage")
    public ResponseEntity<IndexingRepository> indexPage(@RequestParam String url) {
        return ResponseEntity.ok(indexingService.indexingPage(url));
    }

    @GetMapping("/search")
    public ResponseEntity<SearchRepository> search(@RequestParam String query,
                                                   @RequestParam(required = false) Optional<String> site,
                                                   @RequestParam Integer offset,
                                                   @RequestParam Integer limit) {
        return ResponseEntity.ok(searchService.search(query, site, offset, limit));
    }
}
