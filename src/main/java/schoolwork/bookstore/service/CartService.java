package schoolwork.bookstore.service;

import java.util.Map;

public interface CartService {


    Map<Long, Integer> getBooksInCart(long uid);
    boolean clearCart(long uid);
    boolean removeBooksInCart(long uid, long bid);
    boolean updateBooksInCart(long uid, long bid, int number);
    void cartCheckout(long uid);

}
