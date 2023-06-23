package com.project.MyBookShop.model;

import jakarta.persistence.*;

@Entity
public class book2genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;


    @Column(name = "book_id", nullable = false)
    public Integer bookId;

    @Column(name = "genre_id", nullable = false)
    public Integer genreId;
}
