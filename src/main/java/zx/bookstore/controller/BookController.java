package zx.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import zx.bookstore.config.PathConfig;
import zx.bookstore.service.BookService;

@Controller
@RequestMapping("/api/book")
public class BookController {

    PathConfig pathConfig;
    BookService bookService;
    public BookController(BookService bookService, PathConfig pathConfig) {
        this.pathConfig = pathConfig;
        this.bookService = bookService;
    }




}
