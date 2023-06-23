package com.project.MyBookShop.model;

import jakarta.persistence.*;

@Entity
public class document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(columnDefinition = "int default 0", name = "sort_index", nullable = false)
    public Integer sortIndex;

    @Column(columnDefinition = "varchar(255) not null")
    public String slug;

    @Column(columnDefinition = "varchar(255) not null")
    public String title;

    @Column(columnDefinition = "text not null")
    public String text;
}
