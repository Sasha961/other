package com.project.MyBookShop.controllers;

import com.project.MyBookShop.dto.ListBooks;
import com.project.MyBookShop.dto.SearchWord;
import com.project.MyBookShop.model.Book;
import com.project.MyBookShop.model.Book2author;
import com.project.MyBookShop.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public List<Book2author> recommendedBooks() {
        return bookService.getRecommendedBooks(0, 6).getContent();
    }

    @GetMapping("/books/recommended")
    @ResponseBody
    public ListBooks getRecommendedBooksPage(@RequestParam("offset") Integer offset,
                                             @RequestParam("limit") Integer limit) {
        return new ListBooks(bookService.getRecommendedBooks(offset, limit).getContent());
    }

    @ModelAttribute("recentBooks")
    public List<Book2author> recentBooks(){
        return bookService.getRecommendedBooks(0, 10).getContent();
    }

    @GetMapping("/books/recentBooks")
    @ResponseBody
    public ListBooks getRecentBooksPage(@RequestParam("offset") Integer offset,
                                        @RequestParam("limit") Integer limit) {
        return new ListBooks(bookService.getRecommendedBooks(offset, limit).getContent());
    }

    @ModelAttribute("popular")
    public List<Book2author> popularBooks(){
        return bookService.getRecommendedBooks(0, 10).getContent();
    }

    @GetMapping("/books/popularBooks")
    @ResponseBody
    public ListBooks getPopularBooksPage(@RequestParam("offset") Integer offset,
                                        @RequestParam("limit") Integer limit) {
        return new ListBooks(bookService.getRecommendedBooks(offset, limit).getContent());
    }

    @GetMapping(value = {"/search", "/search/{searchWord}"})
    public String getSearchResult(@PathVariable(value = "searchWord", required = false) SearchWord searchWord, Model model){
        model.addAttribute("searchWord", searchWord);
        model.addAttribute("searchResults", bookService.getSearchPage(searchWord.getWord(), 0, 20).getContent());
        return "/search/index";
    }



    @ModelAttribute("searchWord")
    public SearchWord searchWord(){
        return new SearchWord();
    }

    @ModelAttribute("searchResults")
    public List<Book> searchResults(){
        return new ArrayList<>();
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

    @GetMapping("/my")
    public String my(){
        return "/my";
    }

    @GetMapping("/profile")
    public String profile(){
        return "/profile";
    }

    @GetMapping("/myarchive")
    public String myArchive(){
        return "/myarchive";
    }
}
