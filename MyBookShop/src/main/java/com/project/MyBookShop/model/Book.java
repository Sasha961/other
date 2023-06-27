package com.project.MyBookShop.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "book")
public class Book {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer Id;



    @Column(name = "pub_date", nullable = false)
    public Date pubDate;

    @Column(columnDefinition = "smallint not null", name = "is_bestseller")
    public Integer isBestseller;

    @Column(columnDefinition = "varchar(255) not null")
    public String slug;

    @Column(columnDefinition = "varchar(255) not null")
    public String title;

    @Column(columnDefinition = "varchar(255)")
    public String image;

    @Column(columnDefinition = "text")
    public String description;

    @Column(nullable = false)
    public Integer price;

    @Column(columnDefinition = "int not null default 0")
    public  Integer discount;

    @Override
    public String toString() {
        return "book{" +
                "Id=" + Id +
                ", pubDate=" + pubDate +
                ", isBestseller=" + isBestseller +
                ", slug='" + slug + '\'' +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                '}';
    }
}
