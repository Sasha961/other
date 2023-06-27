package com.project.MyBookShop.controllers;

import com.project.MyBookShop.model.Book;
import com.project.MyBookShop.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookRestController {

    private final BookRepository bookRepository;

    @GetMapping("/allBooks")
    public List<Book> allBooks(){
        return bookRepository.findAll();
    }
}
