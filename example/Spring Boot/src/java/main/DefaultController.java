package main;

import model.Book;
import model.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;

//@RestController //контроллер класс для возвращения данных
@Controller
public class DefaultController {

    @Autowired
    private BookRepository bookRepository;

    @Value("${someParameter}")    // для доступа к application.yml или properties
    private Integer someParameter;

    @RequestMapping("/") // путь к запросу как страница на сайте
    public String index(Model model) { // модуль только для html
        Iterable<Book> iterable = bookRepository.findAll();
        ArrayList <Book> books = new ArrayList<>();
        iterable.forEach(book -> books.add(book));
        model.addAttribute("books", books); //для html
        model.addAttribute("booksCount", books.size());
        model.addAttribute("someParameter", someParameter);

//        return new Date().toString();
        return "index"; // для index.html в папке resources/templates
        //<dependency>
        //            <groupId>org.springframework.boot</groupId>
        //            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        //            <version>2.1.4.RELEASE</version>
        //        </dependency>
    }
}
