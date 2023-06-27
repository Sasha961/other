package com.project.MyBookShop.service;

import com.project.MyBookShop.model.Author;
import com.project.MyBookShop.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public Map<String, List<Author>> getAuthorsMap() {
        List<Author> authorList = authorRepository.findAll();
        return authorList.stream().collect(Collectors.groupingBy((Author a)-> a.getName().substring(0,1)));
    }
}
