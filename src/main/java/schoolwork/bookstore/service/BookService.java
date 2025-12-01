package schoolwork.bookstore.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import schoolwork.bookstore.model.Book;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface BookService {
    List<Book> getAllBooks();
    List<Book> getBooksByCondition(String keyword, String author, String tags, Boolean isStock);
    IPage<Book> pageBooks(int curPage, int pageSize);
    IPage<Book> pageBooksByCondition(int curPage, int pageSize, String keyword, String author, String tags, Boolean isStock);
    Book getBookByBid(long bid);
    CompletableFuture<String> getAlRecommendation(String wanting);

}
