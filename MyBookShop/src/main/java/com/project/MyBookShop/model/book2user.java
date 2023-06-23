package com.project.MyBookShop.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class book2user {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(nullable = false)
    public LocalDateTime time;

    @Column(name = "type_id", nullable = false)
    public Integer typeId;

    @Column(name = "book_id", nullable = false)
    public Integer bookId;

    @Column(name = "user_id", nullable = false)
    public Integer userId;
}
