package searchengine.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "page")
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    Site siteId;

    @Column(columnDefinition = "text not null, index pathId (path(512),site_id)")
    String path;

    @Column(nullable = false)
    int code;

    @Column(length = 16777215, columnDefinition = "mediumtext character set utf8mb4 collate utf8mb4_general_ci")
    String content;
}
