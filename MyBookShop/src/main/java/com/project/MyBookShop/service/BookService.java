package com.project.MyBookShop.service;

import com.project.MyBookShop.model.Book;
import com.project.MyBookShop.model.Book2author;
import com.project.MyBookShop.repository.Book2authorRepository;
import com.project.MyBookShop.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final Book2authorRepository book2authorRepository;

    public Page<Book2author> getRecommendedBooks(Integer offset, Integer limit){
        Pageable page = PageRequest.of(offset, limit);
        return book2authorRepository.findAll(page);
    }

    public Page<Book> getSearchPage(String searchWord, Integer offset, Integer limit){
        Pageable page = PageRequest.of(offset, limit);
        return bookRepository.findBookByTitleContaining(searchWord, page);
    }

}
