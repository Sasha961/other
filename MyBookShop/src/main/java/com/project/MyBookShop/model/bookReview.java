package com.project.MyBookShop.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "book_review")
public class bookReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(nullable = false, name = "book_id")
    public Integer bookId;

    @Column(nullable = false, name = "user_id")
    public Integer userID;

    @Column(nullable = false)
    public LocalDateTime time;

    @Column(columnDefinition = "text not null")
    public String text;

}
