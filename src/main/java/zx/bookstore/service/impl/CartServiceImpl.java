package zx.bookstore.service.impl;

import org.springframework.stereotype.Service;
import zx.bookstore.mapper.CartMapper;
import zx.bookstore.service.CartService;

@Service
public class CartServiceImpl implements CartService {
    CartMapper cartMapper;
    public CartServiceImpl(CartMapper cartMapper) {
        this.cartMapper = cartMapper;
    }


}
