package com.project.MyBookShop.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(nullable = false)
    public LocalDateTime time;

    @Column(name = "user_id")
    public Integer userId;

    @Column(columnDefinition = "varchar(255)")
    public String email;

    @Column(columnDefinition = "varchar(255)")
    public String name;

    @Column(columnDefinition = "varchar(255) not null")
    public String subject;

    @Column(columnDefinition = "varchar(255) not null")
    public String text;
}
