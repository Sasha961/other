package main;

import model.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class BookController {

    @Autowired   // автоматически станет доступен для БД
    private BookRepository bookRepository; // для БД

    ///                               Для работы с классом Storage


////    @RequestMapping(value = "books/", method = RequestMethod.GET)  // куда положить и каким методом в данном случае добавить
//      @GetMapping("/books/")   // или так
//    public List<Book> list() { // список книг
//
//        return Storage.getAllBooks();
//    }
//
////    @RequestMapping(value = "/books/", method = RequestMethod.POST) // URL
//    @PostMapping("/books/") // или так
//    public int add(Book book) {
//
//        return Storage.addBook(book);
//    }
//
//    @GetMapping("/books/{id}")
//    public ResponseEntity get(@PathVariable int id){  // Для возвращения ошибки. Получают request и возвращают response
//                        // для того чтобы переменная пришла этой строки
//        Book book = Storage.getBook(id);
//        if (book == null){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // возвращаем ошибку в данном случае 404 при null
//        }
//        return new ResponseEntity(book, HttpStatus.OK); // возвращаем ок
//    }


    //                               Для работы с БД (так же к остальным полям)

    // Добавление в БД
    @PostMapping("/books/")
    public int add(Book book) {
        Book newBook = bookRepository.save(book);
        return newBook.getId();
    }

    // получение из БД
    @GetMapping("/books/")
    public List<Book> list() {

        Iterable<Book> bookIterable = bookRepository.findAll();
        ArrayList<Book> books = new ArrayList<>();
        bookIterable.forEach(book -> books.add(book));
        return books;
    }

        @GetMapping("/books/{id}")
        public ResponseEntity get(@PathVariable int id) {

            Optional<Book> optionalBook = bookRepository.findById(id); // метод для БД

            if (!optionalBook.isPresent()) { // isPresent проверяет, вернул ли объект с базы
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return new ResponseEntity(optionalBook, HttpStatus.OK);
        }

        // удаление из БД

    public void delete(){
//        bookRepository.delete(book);  удаление по обьекту
//        bookRepository.delete(12); удаление по id
//        bookRepository.save(book); замена по id
    }

}
