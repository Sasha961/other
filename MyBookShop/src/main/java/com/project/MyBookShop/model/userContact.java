package com.project.MyBookShop.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_contact")
public class userContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Column(name = "user_id", nullable = false)
    public Integer userId;

    public String type;

    @Column(columnDefinition = "smallint not null")
    public Integer approved;

    @Column(columnDefinition = "varchar(255)")
    public String code;

    @Column(name = "code_trails")
    public Integer codeTrails;

    @Column(name = "code_time")
    public LocalDateTime codeTime;

    @Column(columnDefinition = "varchar(255) not null")
    public String contact;
}
