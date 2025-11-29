package schoolwork.bookstore.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;
import schoolwork.bookstore.config.PathConfig;
import schoolwork.bookstore.model.Book;
import schoolwork.bookstore.dto.PageResponse;
import schoolwork.bookstore.dto.Result;
import schoolwork.bookstore.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/list")
    public Result getBooks() {
        List<Book> books = bookService.getAllBooks();
        return Result.success(books);
    }

    /**
     * 分页查询
     */
    @GetMapping
    public Result getBooks(@RequestParam int page, @RequestParam int size) {
        IPage<Book> books = bookService.pageBooks(page, size);
        PageResponse<Book> response = new PageResponse<>(page,size, books.getTotal(), books.getRecords());
        return Result.success(response);
    }

    @GetMapping("/search")
    public Result getBooksByCondition(@RequestParam(required = false) Integer page,
                                      @RequestParam(required = false) Integer size,
                                      @RequestParam(required = false) String keyword,
                                      @RequestParam(required = false) String author,
                                      @RequestParam(required = false) String tags,
                                      @RequestParam(required = false) Boolean isStock){

        if(page == null || size == null)

            return Result.success(bookService.getBooksByCondition(keyword, author, tags, isStock));
        else{
            IPage<Book> books = bookService.pageBooksByCondition(page,size,keyword, author, tags, isStock);
            PageResponse<Book> response = new PageResponse<>(page,size, books.getTotal(), books.getRecords());
            return Result.success(response);
        }

    }
}
