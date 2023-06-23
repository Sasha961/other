package com.project.MyBookShop.model;

import jakarta.persistence.*;

@Entity
public class genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(name = "parent_id")
    public Integer parentId;

    @Column(columnDefinition = "varchar(255) not null")
    public String slug;

    @Column(columnDefinition = "varchar(255) not null")
    public String name;
}
