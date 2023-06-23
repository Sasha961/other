package com.project.MyBookShop.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(columnDefinition = "varchar(255) not null")
    public Integer hash;

    @Column(name = "reg_time", nullable = false)
    public LocalDateTime regTime;

    @Column(nullable = false)
    public Integer balance;

    @Column(columnDefinition = "varchar(255)")
    public String name;
}
