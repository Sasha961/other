package com.project.MyBookShop.repository;

import com.project.MyBookShop.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

//    Page<book> findAllByPubDate(Pageable page);
}
