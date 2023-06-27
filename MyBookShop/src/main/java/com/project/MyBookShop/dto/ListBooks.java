package com.project.MyBookShop.dto;

import com.project.MyBookShop.model.Book2author;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListBooks {

    public Integer count;

    public List<Book2author> books;

    public ListBooks(List<Book2author> books) {
        this.count = books.size();
        this.books = books;
    }
}
