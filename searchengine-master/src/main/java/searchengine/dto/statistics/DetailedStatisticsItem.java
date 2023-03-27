package searchengine.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import searchengine.dto.indexing.EnumStatusAtSite;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailedStatisticsItem {
    private String url;
    private String name;
    private EnumStatusAtSite status;
    private LocalDateTime statusTime;
    private String error;
    private int pages;
    private int lemmas;
}
