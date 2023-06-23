package com.project.MyBookShop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/books")
public class BookController {

    @GetMapping("/recent")
    public String recentPage(){
        return "/books/recent";
    }

    @GetMapping("/popular")
    public String popularPage(){
        return "/books/popular";
    }

//    @GetMapping("/author")
//    public String authorPage(){
//        return "books/author";
//    }

}
