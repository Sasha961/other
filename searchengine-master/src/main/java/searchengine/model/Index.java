package searchengine.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "`index`", indexes = @javax.persistence.Index(name = "multiIndex", columnList = "page_id, lemma_id"))
public class Index {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "page_id", nullable = false)
    Page pageId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "lemma_id", nullable = false)
    Lemma lemmaId;

    @Column(name = "`rank`", nullable = false)
    float rank;
}
