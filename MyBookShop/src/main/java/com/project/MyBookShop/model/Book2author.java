package com.project.MyBookShop.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Book2author {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
    public Book bookId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
    public Author authorId;

    @Column(name = "sort_index", columnDefinition = "int not null default 0")
    public Integer sortIndex;
}
