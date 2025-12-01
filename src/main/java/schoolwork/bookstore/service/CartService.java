package schoolwork.bookstore.service;

public interface CartService {

    boolean clearCart(long uid);
    boolean removeBooksInCart(long uid, long bid);
    boolean updateBooksInCart(long uid, long bid, int number);
    void cartCheckout(long uid);

}
