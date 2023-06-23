package com.project.MyBookShop.model;

import jakarta.persistence.*;

@Entity
@Table(name = "file_download")
public class fileDownload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(name = "user_id", nullable = false)
    public Integer userId;

    @Column(name = "book_id", nullable = false)
    public Integer bookId;

    @Column(columnDefinition = "int not null default 1")
    public Integer count;
}
