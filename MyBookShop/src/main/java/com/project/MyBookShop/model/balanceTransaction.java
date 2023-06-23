package com.project.MyBookShop.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "balance_transaction")
public class balanceTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(name = "user_id", nullable = false)
    public Integer userId;

    @Column(nullable = false)
    public LocalDateTime time;

    @Column(columnDefinition = "int not null default 0")
    public Integer value;

    @Column(name = "book_id", nullable = false)
    public Integer bookId;

    @Column(columnDefinition = "TEXT", nullable = false)
    public String description;
}
