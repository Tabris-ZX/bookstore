package schoolwork.bookstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.mindrot.jbcrypt.BCrypt;
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
            password = BCrypt.hashpw(password, BCrypt.gensalt(10));
            User user = new User(username, password);
            UserInfo userInfo = new UserInfo(user.getUid());
            return userMapper.insert(user) == 1&& userInfoMapper.insert(userInfo) == 1;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public User loginByUsername(String username, String password) {
        User user = userMapper.getUserByUsername(username);
        if(user == null||!BCrypt.checkpw(password, user.getPassword())) return null;
        return user;
    }

    @Override
    public User loginByUid(long uid, String password) {
        User user = userMapper.getUserByUid(uid);
        if(user == null||!BCrypt.checkpw(password, user.getPassword())) return null;
        return user;
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
    public boolean changeAuth(long uid, String type, String content) {
        if (content.isEmpty()) return false;
        try{
            if(type.equals("username")) userMapper.updateUsername(uid,content);
            else if(type.equals("password")) userMapper.updatePassword(uid,content);
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
