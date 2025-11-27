package schoolwork.bookstore.service;

public interface CartService {

    boolean clearCart(long uid);
    boolean addBooksToCart(long bid, int number);
    boolean updateBooksNumberInCart(long uid, long bid,int number);
    boolean updateBooksInCart(long uid, long bid, int number);
    boolean buyBooksInCart(long uid);

}
