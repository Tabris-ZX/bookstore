package schoolwork.bookstore.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;
import schoolwork.bookstore.config.PathConfig;
import schoolwork.bookstore.model.Book;
import schoolwork.bookstore.pojo.BookQuery;
import schoolwork.bookstore.pojo.PageResponse;
import schoolwork.bookstore.pojo.Result;
import schoolwork.bookstore.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    PathConfig pathConfig;
    BookService bookService;
    public BookController(BookService bookService, PathConfig pathConfig) {
        this.pathConfig = pathConfig;
        this.bookService = bookService;
    }

    @GetMapping("/list")
    public Result getBooks() {
        List<Book> books = bookService.getAllBooks();
        return Result.success(books);
    }

    @GetMapping("/list")
    public Result getBooks(@RequestParam int curPage, @RequestParam int size) {
        IPage<Book> books = bookService.pageBooks(curPage, size);
        PageResponse<Book> response = new PageResponse<>(curPage,size, books.getTotal(), books.getRecords());
        return Result.success(response);
    }

    @GetMapping("/search")
    public Result getBooksByCondition(BookQuery condition) {
        List<Book> books = bookService.getBooksByCondition(condition);
        return Result.success(books);
    }

//    @GetMapping("/")
//    public Result getBooksByCondition(@RequestParam int curPage, @RequestParam int size, BookQuery condition) {
//        IPage<Book> books = bookService.pageBooksByCondition(curPage, size, condition);
//        PageResponse<Book> response = new PageResponse<>(curPage,size, books.getTotal(), books.getRecords());
//        return Result.success(response);
//    }

}
