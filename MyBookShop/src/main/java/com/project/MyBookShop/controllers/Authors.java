package com.project.MyBookShop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/authors")
public class Authors {
    @GetMapping("/index")
    public String authors(){
        return "/authors/index";
    }
}
