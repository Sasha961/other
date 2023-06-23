package com.project.MyBookShop.service;

import com.project.MyBookShop.model.Book;
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

    public Page<Book> getRecommendedBooks(Integer offset, Integer limit){
        Pageable page = PageRequest.of(offset, limit);
        return bookRepository.findAll(page);
    }

    public boolean isSale(Book book) {
        return (float) (book.getPrice() / book.getDiscount()) * 100 > 30;
    }

//    public Page<book> getRecentBooks(Integer offset, Integer limit){
//        Pageable page = PageRequest.of(offset, limit);
//        return bookRepository.findAllByPubDate(page);
//    }
//
//    public List<book> getAll(){
//        return bookRepository.findAll();
//    }

}
