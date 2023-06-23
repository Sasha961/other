package com.project.MyBookShop.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "book_review_like")
public class bookReviewLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(nullable = false, name = "review_id")
    public Integer reviewId;

    @Column(nullable = false, name = "user_id")
    public Integer userId;

    @Column(nullable = false)
    public LocalDateTime time;

    @Column(columnDefinition = "smallint not null")
    public Integer value;
}
