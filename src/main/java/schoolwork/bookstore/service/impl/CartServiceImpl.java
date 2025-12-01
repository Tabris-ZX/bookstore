package schoolwork.bookstore.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import schoolwork.bookstore.mapper.BookMapper;
import schoolwork.bookstore.mapper.CartMapper;
import schoolwork.bookstore.mapper.OrderMapper;
import schoolwork.bookstore.model.Order;
import schoolwork.bookstore.service.CartService;

import java.util.Map;

@Service
public class CartServiceImpl implements CartService {
    private final BookMapper bookMapper;
    CartMapper cartMapper;
    OrderMapper orderMapper;
    public CartServiceImpl(CartMapper cartMapper, OrderMapper orderMapper, BookMapper bookMapper) {
        this.cartMapper = cartMapper;
        this.orderMapper = orderMapper;
        this.bookMapper = bookMapper;
    }

    @Override
    public boolean clearCart(long uid) {
        return cartMapper.clearCart(uid);
    }

    @Override
    public boolean removeBooksInCart(long uid,long bid) {
        return cartMapper.removeBooksInCart(uid, bid);
    }

    @Override
    public boolean updateBooksInCart(long uid, long bid, int number) {
        return cartMapper.insertOrUpdateBooksInCart(uid, bid, number);
    }

    @Override
    @Transactional
    public void cartCheckout(long uid) {

        Map<Long, Integer> cartInfo = cartMapper.getCartBooks(uid);
        if (cartInfo == null || cartInfo.isEmpty()) {throw new RuntimeException("购物车为空");}
        for (var book : cartInfo.entrySet()) {
            int rows = bookMapper.solveSold(book.getKey(),book.getValue());
            if (rows == 0) {
                throw new RuntimeException("图书 " + book.getKey() + " 库存不足");
            }
        }
        double totalPrice = cartMapper.getCartTotalPrice(uid);
        Order order = new Order(uid, cartInfo, totalPrice);
        orderMapper.insert(order);
        cartMapper.clearCart(uid);
    }


//    public boolean checkStock(long bid, int number) {
//        return bookMapper.checkStock(bid,number);
//    }

//    public boolean checkout(long uid) {
//        return buyBooksInCart(uid);
//    }


}
