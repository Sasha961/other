package com.project.MyBookShop.repository;

import com.project.MyBookShop.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    Page<Book> findBookByTitleContaining(String word, Pageable page);
}
