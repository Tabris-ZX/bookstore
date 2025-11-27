package schoolwork.bookstore.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import schoolwork.bookstore.model.Book;
import schoolwork.bookstore.pojo.BookQuery;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();
    List<Book> getBooksByCondition(BookQuery bookQuery);
    IPage<Book> pageBooks(int curPage, int pageSize);
    IPage<Book> pageBooksByCondition(int curPage, int pageSize, BookQuery bookQuery);
    Book getBookById(long bid);
    List<Book> searchBooksByKeyword(String keyword);
    List<Book> searchBooksByCondition(BookQuery bookQuery);
}
