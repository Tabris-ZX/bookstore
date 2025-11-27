package schoolwork.bookstore.service;

import schoolwork.bookstore.model.Book;
import schoolwork.bookstore.model.User;

import java.util.List;

public interface AdminService {
    List<User> getAllUsers();
    boolean banUser(long uid);
    boolean unBanUser(long uid);
    boolean addUser(String username, String password);
    boolean removeUser(long uid);

    boolean addBook(Book book);
    boolean removeBook(long bid);
    boolean updateBookInfo(Book book);
    boolean updateBookStock(long bid, int curStock);


}
