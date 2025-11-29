package schoolwork.bookstore.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import schoolwork.bookstore.model.Book;
import schoolwork.bookstore.model.User;

import java.util.List;

public interface AdminService {
    List<User> getAllUsers();
    IPage<User> pageUsers(int curPage, int pageSize);
    boolean banUser(long uid);
    boolean unBanUser(long uid);
    boolean addUser(User user);
    boolean removeUser(long uid);

    boolean addBook(Book book);
    boolean removeBook(long bid);
    boolean updateBookInfo(Book book);
    boolean updateBookStock(long bid, int curStock);


}
