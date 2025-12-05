package schoolwork.bookstore.service;

import schoolwork.bookstore.dto.CartBooks;

import java.util.List;
import java.util.Map;

public interface CartService {


    List<CartBooks> getBooksInCart(long uid);
    boolean clearCart(long uid);
    boolean removeBooksInCart(long uid, long bid);
    boolean updateBooksInCart(long uid, long bid, int number);
    void cartCheckout(long uid);

}
