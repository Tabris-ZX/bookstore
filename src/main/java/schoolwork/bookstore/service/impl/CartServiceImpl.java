package schoolwork.bookstore.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import schoolwork.bookstore.mapper.CartMapper;
import schoolwork.bookstore.mapper.OrderMapper;
import schoolwork.bookstore.model.Order;
import schoolwork.bookstore.service.CartService;

import java.util.Map;

@Service
public class CartServiceImpl implements CartService {
    CartMapper cartMapper;
    OrderMapper orderMapper;
    public CartServiceImpl(CartMapper cartMapper, OrderMapper orderMapper) {
        this.cartMapper = cartMapper;
        this.orderMapper = orderMapper;
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
    public boolean buyBooksInCart(long uid) {
        try{
            Map<Long,Integer> cartInfo = cartMapper.getCartBooks(uid);
            double totalPrice = cartMapper.getCartTotalPrice(uid);
            cartMapper.clearCart(uid);
            Order order = new Order(uid,cartInfo, totalPrice);
            orderMapper.insert(order);
        }catch (Exception e){
            return false;
        }
        return true;
    }
//
//    private double solvePrice(Map<Long,Integer> cartInfo){
//        double total = 0.0;
//        for (Map.Entry<Long, Integer> entry : cartInfo.entrySet()) {
//            Long bid = entry.getKey();
//            Integer number = entry.getValue();
//            double price = 10.0;
//            total += price * number;
//        }
//        return total;
//    }


}
