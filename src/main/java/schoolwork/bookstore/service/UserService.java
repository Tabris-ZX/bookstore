package schoolwork.bookstore.service;

import schoolwork.bookstore.model.Order;
import schoolwork.bookstore.model.User;
import schoolwork.bookstore.model.UserAddr;

public interface UserService {


    User getUserByUid(long uid);
    UserAddr getUserAddr(long uid);
    boolean register(String username, String password);
    User loginByUsername(String username, String password);
    User loginByUid(long uid, String password);
    boolean changeAuth(long uid, String type, String content);
    boolean updateInfo(UserAddr userAddr);
    boolean forgetPassword(String username);
    boolean addBooksToCart(long uid, long bid, int number);
    boolean buyBooks(long uid, long bid, int number);

    boolean submitCartOrder(Order order);
}
