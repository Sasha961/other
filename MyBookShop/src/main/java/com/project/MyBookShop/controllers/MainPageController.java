package com.project.MyBookShop.controllers;

import com.project.MyBookShop.dto.ListBooks;
import com.project.MyBookShop.model.Book;
import com.project.MyBookShop.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class MainPageController {

    private final BookService bookService;

    @GetMapping("/")
    public String mainPage() {
        return "index";
    }

    @ModelAttribute("recommendedBooks")
    public List<Book> recommendedBooks() {
        return bookService.getRecommendedBooks(0, 6).getContent();
    }

    @GetMapping("/book/recommended")
    @ResponseBody
    public ListBooks getBooksPage(@RequestParam("offset") Integer offset,
                                  @RequestParam("limit") Integer limit) {
        return new ListBooks(bookService.getRecommendedBooks(offset, limit).getContent());
    }

    @ModelAttribute("booksList")
    public List<Book> bookList(){
        return bookService.getRecommendedBooks(0, 10).getContent();
    }

    @ModelAttribute("recentBooks")
    public List<Book> recentBooks() {
        return bookService.getRecommendedBooks(0, 10).getContent();
    }

    @GetMapping("/signin")
    public String signIn(){
        return "signin";
    }

    @GetMapping("/documents/index")
    public String documentsIndex(){
        return "/documents/index";
    }

    @GetMapping("/about")
    public String about(){
        return "/about";
    }

    @GetMapping("/faq")
    public String faq(){
        return "/faq";
    }

    @GetMapping("/contacts")
    public String contacts(){
        return "/contacts";
    }

    @GetMapping("/cart")
    public String cart(){
        return "/cart";
    }

    @GetMapping("/postponed")
    public String postponed(){
        return "/postponed";
    }


//    @ModelAttribute("isSale")
//    public boolean isSale(Book book){
//        return bookService.isSale(book);
//    }
//
//    @GetMapping("/books/recent")
//    public ListBooks getRecentBooks(@RequestParam("offset") Integer offset,
//                                    @RequestParam("limit") Integer limit){
//        return new ListBooks(bookService.getRecentBooks(offset, limit).getContent());
//    }

}
