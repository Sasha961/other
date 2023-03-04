package searchengine.model;

import lombok.*;
import searchengine.dto.indexing.EnumStatusAtSite;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "site")
public class SiteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @OneToMany(mappedBy = "siteId", cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    List<PageEntity> pageEntities;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('INDEXING', 'INDEXED', 'FAILED')")
    EnumStatusAtSite status;

    @Column(name = "status_time", nullable = false)
    LocalDateTime statusTime;

    @Column(name = "last_error", columnDefinition = "TEXT")
    String lastError;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    String url;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    String name;
}
