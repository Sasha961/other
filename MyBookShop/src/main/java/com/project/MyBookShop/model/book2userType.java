package com.project.MyBookShop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "book2user_type")
public class book2userType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(columnDefinition = "varchar(255) not null")
    public String code;

    @Column(columnDefinition = "varchar(255) not null")
    public String name;
}
