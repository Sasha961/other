package com.project.MyBookShop.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Entity
public class book2author {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer Id;

    @Column(name = "book_id", nullable = false)
    public Integer bookId;

    @Column(name = "author_id", nullable = false)
    public Integer authorId;

    @Column(name = "sort_index", columnDefinition = "int not null default 0")
    public Integer sortIndex;
}
