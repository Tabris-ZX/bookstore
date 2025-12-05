package schoolwork.bookstore.service;

import schoolwork.bookstore.model.Order;
import schoolwork.bookstore.model.User;
import schoolwork.bookstore.model.UserAddr;

import java.util.concurrent.CompletableFuture;

public interface UserService {


    User getUserByUid(long uid);
    UserAddr getUserAddr(long uid);
    boolean register(String username, String password);
    User loginByUsername(String username, String password);
    User loginByUid(long uid, String password);
    boolean changeAuth(long uid, String type, String content);
    boolean updateInfo(UserAddr userAddr);
    boolean forgetPassword(String username);
    boolean addBooksToCart(long uid, long bid, int number);
    boolean buyBooks(long uid, long bid, int number);
    boolean submitCartOrder(Order order);
    CompletableFuture<String> getAlRecommendation(String wanting,long uid);
    /**
     * 流式获取AI推荐
     * @param wanting 用户需求
     * @param uid 用户ID
     * @param onChunk 处理每个数据块的回调函数
     */
    void getAlRecommendationStream(String wanting, long uid, java.util.function.Consumer<String> onChunk);
}
