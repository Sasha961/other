package com.project.MyBookShop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "book_file")
public class bookFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(columnDefinition = "varchar(255) not null")
    public String hash;

    @Column(name = "type_id", nullable = false)
    public Integer typeId;

    @Column(columnDefinition = "varchar(255) not null")
    public String path;
}
