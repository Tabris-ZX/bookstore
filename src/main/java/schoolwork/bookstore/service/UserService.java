package schoolwork.bookstore.service;

import schoolwork.bookstore.model.Order;
import schoolwork.bookstore.model.User;

public interface UserService {


    User getUserByUid(long uid);
    boolean register(String username, String password);
    User login(String username, String password);
    boolean updateInfo(User user);
    boolean forgetPassword(String username);
    boolean addBooksToCart(long uid, long bid, int number);
    boolean buyBooks(long uid, long bid, int number);

    boolean submitCartOrder(Order order);
}
