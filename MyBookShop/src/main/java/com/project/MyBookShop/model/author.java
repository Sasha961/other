package com.project.MyBookShop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "author")
public class author {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer Id;

    @Column(columnDefinition = "varchar(255)")
    public String photo;

    @Column(columnDefinition = "varchar(255) not null")
    public String slug;

    @Column(columnDefinition = "varchar(255) not null")
    public String name;

    @Column(columnDefinition = "text")
    public String description;
}
