package searchengine.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lemma")
public class LemmaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int id;

    @OneToMany(mappedBy = "lemmaId", cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    List<IndexEntity> index;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    SiteEntity site;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    String lemma;

    @Column(nullable = false)
    int frequency;
}
