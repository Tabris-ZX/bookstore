package schoolwork.bookstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import schoolwork.bookstore.mapper.*;
import schoolwork.bookstore.model.*;
import schoolwork.bookstore.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final CartMapper cartMapper;
    BookMapper bookMapper;
    UserMapper userMapper;
    UserInfoMapper userInfoMapper;
    OrderMapper orderMapper;
    public  UserServiceImpl(UserMapper userMapper, OrderMapper orderMapper, UserInfoMapper userInfoMapper, BookMapper bookMapper, CartMapper cartMapper) {
        this.userMapper = userMapper;
        this.userInfoMapper = userInfoMapper;
        this.orderMapper = orderMapper;
        this.bookMapper = bookMapper;
        this.cartMapper = cartMapper;
    }

    @Override
    public User getUserByUid(long uid) {
        return userMapper.selectOne(new LambdaUpdateWrapper<User>().eq(User::getUid,uid));
    }

//    public UserInfo getUserProfile(long uid) {
//        return userInfoMapper.selectById(uid);
//    }

    @Override
    @Transactional
    public boolean register(String username, String password) {
        try{
            User user = new User(username, password);
            UserInfo userInfo = new UserInfo(user.getUid());
            return userMapper.insert(user) == 1&& userInfoMapper.insert(userInfo) == 1;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public User loginByUsername(String username, String password) {
        return userMapper.getUserByUsername(username, password);
    }

    @Override
    public User loginByUid(long uid, String password) {
        return userMapper.getUserByUid(uid, password);
    }

    @Override
    public boolean updateInfo(UserInfo userInfo) {
        LambdaUpdateWrapper<UserInfo> updateWrapper = new LambdaUpdateWrapper<>();
        if(userInfo.getName()!=null)  updateWrapper.set(UserInfo::getName,userInfo.getName());
        if(userInfo.getEmail()!=null) updateWrapper.set(UserInfo::getEmail,userInfo.getEmail());
        if(userInfo.getPhone()!=null) updateWrapper.set(UserInfo::getPhone,userInfo.getPhone());
        if(userInfo.getAddress()!=null) updateWrapper.set(UserInfo::getAddress,userInfo.getAddress());
        return userInfoMapper.update(userInfo,new LambdaUpdateWrapper<UserInfo>().eq(UserInfo::getUid, userInfo.getUid()))==1;
    }

    @Transactional
    public boolean changeAuth(long uid, String username, String password) {
        try{
            if(username!=null) userMapper.updateUsername(uid,username);
            if(password!=null) userMapper.updatePassword(uid,password);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public boolean forgetPassword(String username) {
        return false;
    }

    @Override
    public boolean addBooksToCart(long uid, long bid, int number) {
        Cart cart = new  Cart(uid, bid, number);
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
