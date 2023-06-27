package com.project.MyBookShop.repository;

import com.project.MyBookShop.model.Book2author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Book2authorRepository extends JpaRepository<Book2author, Integer> {

}
