package schoolwork.bookstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import schoolwork.bookstore.mapper.BookMapper;
import schoolwork.bookstore.mapper.CartMapper;
import schoolwork.bookstore.mapper.OrderMapper;
import schoolwork.bookstore.mapper.UserMapper;
import schoolwork.bookstore.model.Book;
import schoolwork.bookstore.model.Cart;
import schoolwork.bookstore.model.Order;
import schoolwork.bookstore.model.User;
import schoolwork.bookstore.service.UserService;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final CartMapper cartMapper;
    BookMapper bookMapper;
    UserMapper userMapper;
    OrderMapper orderMapper;
    public  UserServiceImpl(UserMapper userMapper, OrderMapper orderMapper, BookMapper bookMapper, CartMapper cartMapper) {
        this.userMapper = userMapper;
        this.orderMapper = orderMapper;
        this.bookMapper = bookMapper;
        this.cartMapper = cartMapper;
    }

    public User getUserByUid(long uid) {
        return userMapper.selectById(uid);
    }


    @Override
    public boolean register(String username, String password) {

        User user = new User(username, password);

        return userMapper.insert(user) == 1;
    }

    @Override
    public User login(String username, String password) {
        return userMapper.findUser(username, password);
    }

    @Override
    public boolean updateInfo(User user) {
        return userMapper.update(user,new LambdaUpdateWrapper<User>().eq(User::getUid, user.getUid()))==1;
    }

    @Override
    public boolean forgetPassword(String username) {
        return false;
    }

    @Override
    public boolean addBooksToCart(long uid, long bid, int number) {
        Cart cart = new  Cart(uid, bid, number);
        cart.setId(UUID.randomUUID().toString());
        return cartMapper.insert(cart)==1;
    }

    @Override
    public boolean buyBooks(long uid, long bid, int number) {
        Order order = new Order(uid, bid, number);
        order.setTotalPrice(bookMapper.selectById(bid).getPrice()*number);
        return orderMapper.insert(order) == 1;
    }

    @Override
    public boolean submitCartOrder(Order order) {
        return orderMapper.insert(order) == 1;
    }
}
