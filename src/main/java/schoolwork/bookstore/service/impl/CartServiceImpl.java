package schoolwork.bookstore.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import schoolwork.bookstore.dto.CartBooks;
import schoolwork.bookstore.mapper.BookMapper;
import schoolwork.bookstore.mapper.CartMapper;
import schoolwork.bookstore.mapper.OrderMapper;
import schoolwork.bookstore.mapper.UserAddrMapper;
import schoolwork.bookstore.model.Order;
import schoolwork.bookstore.service.CartService;

import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {
    private final BookMapper bookMapper;
    private final UserAddrMapper userAddrMapper;
    CartMapper cartMapper;
    OrderMapper orderMapper;
    public CartServiceImpl(CartMapper cartMapper, OrderMapper orderMapper, BookMapper bookMapper, UserAddrMapper userAddrMapper) {
        this.cartMapper = cartMapper;
        this.orderMapper = orderMapper;
        this.bookMapper = bookMapper;
        this.userAddrMapper = userAddrMapper;
    }

    @Override
    public List<CartBooks> getBooksInCart(long uid) {
        return cartMapper.getCartBooks(uid);
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
        List<CartBooks> cartInfo = cartMapper.getCartBooks(uid);
        if (cartInfo == null || cartInfo.isEmpty()) {throw new RuntimeException("购物车为空");}
        for (CartBooks book : cartInfo) {
            int rows = bookMapper.solveSold(book.getBid(),book.getNumber());
            if (rows == 0) {
                throw new RuntimeException("图书 " + book.getBid() + " 库存不足");
            }
        }
        double totalPrice = cartMapper.getCartTotalPrice(uid);
        Order order = new Order(uid, cartInfo, totalPrice,userAddrMapper.selectById(uid));
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
