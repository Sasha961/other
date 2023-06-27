package com.project.MyBookShop.controllers;

import com.project.MyBookShop.model.Author;
import com.project.MyBookShop.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorsController {

    private final AuthorService authorService;

    @ModelAttribute("authorsMap")
    public Map<String, List<Author>> authorsMap(){
        return authorService.getAuthorsMap();
    }

    @GetMapping("/index")
    public String authors(){
        return "/authors/index";
    }
}
