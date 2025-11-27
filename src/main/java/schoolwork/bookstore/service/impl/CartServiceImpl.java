package schoolwork.bookstore.service.impl;

import org.springframework.stereotype.Service;
import schoolwork.bookstore.mapper.CartMapper;
import schoolwork.bookstore.service.CartService;

@Service
public class CartServiceImpl implements CartService {
    CartMapper cartMapper;
    public CartServiceImpl(CartMapper cartMapper) {
        this.cartMapper = cartMapper;
    }

    @Override
    public boolean clearCart(long uid) {
        return false;
    }

    @Override
    public boolean addBooksToCart(long bid, int number) {
        return false;
    }

    @Override
    public boolean updateBooksNumberInCart(long uid, long bid, int number) {
        return cartMapper.updateBooksNumberInCart(uid, bid, number) > 0;
    }

    @Override
    public boolean updateBooksInCart(long uid, long bid, int number) {
        return false;
    }

    @Override
    public boolean buyBooksInCart(long uid) {
        return false;
    }

}
