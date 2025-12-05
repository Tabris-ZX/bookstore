package schoolwork.bookstore.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import schoolwork.bookstore.dto.AIData;
import schoolwork.bookstore.dto.AIMemory;
import schoolwork.bookstore.mapper.*;
import schoolwork.bookstore.model.*;
import schoolwork.bookstore.service.UserService;
import schoolwork.bookstore.util.AIUtil;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final CartMapper cartMapper;
    BookMapper bookMapper;
    UserMapper userMapper;
    UserAddrMapper userAddrMapper;
    OrderMapper orderMapper;
    ChatHistoryMapper chatHistoryMapper;
    AIUtil aiUtil;
    public  UserServiceImpl(UserMapper userMapper,
                            OrderMapper orderMapper,
                            UserAddrMapper userAddrMapper,
                            BookMapper bookMapper,
                            CartMapper cartMapper,
                            ChatHistoryMapper chatHistoryMapper,
                            AIUtil aiUtil) {
        this.userMapper = userMapper;
        this.userAddrMapper = userAddrMapper;
        this.orderMapper = orderMapper;
        this.bookMapper = bookMapper;
        this.cartMapper = cartMapper;
        this.chatHistoryMapper = chatHistoryMapper;
        this.aiUtil = aiUtil;
    }

    @Override
    public User getUserByUid(long uid) {
        return userMapper.getUserByUid(uid);
    }

    public UserAddr getUserAddr(long uid) {
        return userAddrMapper.selectOne(new LambdaQueryWrapper<UserAddr>().eq(UserAddr::getUid,uid));
    }

    @Override
    @Transactional
    public boolean register(String username, String password) {
        try{
            password = BCrypt.hashpw(password, BCrypt.gensalt(10));
            User user = new User(username, password);
            UserAddr userAddr = new UserAddr(user.getUid());
            return userMapper.insert(user) == 1&& userAddrMapper.insert(userAddr) == 1;
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
    public boolean updateInfo(UserAddr userAddr) {
        LambdaUpdateWrapper<UserAddr> updateWrapper = new LambdaUpdateWrapper<>();
        if(userAddr.getName()!=null)  updateWrapper.set(UserAddr::getName, userAddr.getName());
        if(userAddr.getPhone()!=null) updateWrapper.set(UserAddr::getPhone, userAddr.getPhone());
        if(userAddr.getAddress()!=null) updateWrapper.set(UserAddr::getAddress, userAddr.getAddress());
        return userAddrMapper.update(userAddr,new LambdaUpdateWrapper<UserAddr>().eq(UserAddr::getUid, userAddr.getUid()))==1;
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
        double price = bookMapper.selectById(bid).getPrice()*number;
        Cart cart = new  Cart(uid, bid, number,price);
        return cartMapper.insert(cart)==1;
    }

    @Override
    @Transactional
    public boolean buyBooks(long uid, long bid, int number) {
        try{
            double price = bookMapper.selectById(bid).getPrice()*number;
            int rows = bookMapper.solveSold(bid, number);
            if (rows == 0) throw new RuntimeException("图书 " + bid + " 库存不足");
            Order order = new Order(uid, bid, number,price,userAddrMapper.selectById(uid));
            return orderMapper.insert(order) == 1;
        }catch (Exception e){
            log.error(e.getMessage());
            return  false;
        }
    }

    @Override
    public boolean submitCartOrder(Order order) {
        return orderMapper.insert(order) == 1;
    }

    @Override
    public CompletableFuture<String> getAlRecommendation(String wanting, long uid) {
        List<AIData> books = bookMapper.getBookInfoForAI();
        List<AIMemory> chat =chatHistoryMapper.getHistoryForAI(uid);
        String bookInfo = JSON.toJSONString(books);
        String chatInfo = JSON.toJSONString(chat);
        return aiUtil.chat(wanting,bookInfo,chatInfo).thenApply(answer->{
                    ChatHistory chatHistory= new ChatHistory(uid,wanting,answer);
                    chatHistoryMapper.insert(chatHistory);
                    return answer;
        });
    }

    @Override
    public void getAlRecommendationStream(String wanting, long uid, Consumer<String> onChunk) {
        List<AIData> books = bookMapper.getBookInfoForAI();
        List<AIMemory> chat = chatHistoryMapper.getHistoryForAI(uid);
        String bookInfo = JSON.toJSONString(books);
        String chatInfo = JSON.toJSONString(chat);
        StringBuilder fullAnswer = new StringBuilder();
        
        aiUtil.chatStream(wanting,bookInfo,chatInfo, chunk -> {
            fullAnswer.append(chunk);
            onChunk.accept(chunk);
        });
        
        if (!fullAnswer.isEmpty()) {
            ChatHistory chatHistory = new ChatHistory(uid, wanting, fullAnswer.toString());
            chatHistoryMapper.insert(chatHistory);
        }
    }
}
