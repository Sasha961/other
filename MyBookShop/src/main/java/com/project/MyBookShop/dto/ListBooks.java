package com.project.MyBookShop.dto;

import com.project.MyBookShop.model.Book;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListBooks {
    Integer count;

    List<Book> books;

    public ListBooks(List<Book> books) {
        this.count = books.size();
        this.books = books;
    }
}
