package com.project.MyBookShop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "book_file_type")
public class bookFileType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(columnDefinition = "varchar(255) not null")
    public String name;

    @Column(columnDefinition = "text")
    public String description;
}
