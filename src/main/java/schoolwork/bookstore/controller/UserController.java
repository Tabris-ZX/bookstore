package schoolwork.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import schoolwork.bookstore.model.User;
import schoolwork.bookstore.pojo.Result;
import schoolwork.bookstore.service.CartService;
import schoolwork.bookstore.service.UserService;
import schoolwork.bookstore.util.JwtUtil;

@RestController
@RequestMapping("/api/users")
public class UserController {

    UserService userService;
    CartService cartService;
    JwtUtil jwtUtil;

    public UserController(UserService userService, CartService cartService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.cartService = cartService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user){
        boolean result = userService.register(user.getUsername(), user.getPassword());
        return result ? Result.success() : Result.error("注册失败");
    }

    @PostMapping("/login")
    public Result login(@RequestBody User user){
        User cur = userService.login(user.getUsername(), user.getPassword());
        if (cur != null){
            String token = jwtUtil.getJwt(cur);
            return  Result.success(token);
        }else {
            return Result.error("用户不存在或密码错误");
        }
    }

    @GetMapping("/{uid}")
    public Result getUser(@PathVariable long uid) {
        return Result.success(userService.getUserByUid(uid));
    }

    @PutMapping("/{uid}")
    public Result updateUser(@RequestBody User user) {
        boolean result = userService.updateInfo(user);
        return result ? Result.success() : Result.error("更新用户信息失败");
    }

    @GetMapping("/{uid}/cart/add")
    public Result addBooksToCart(@PathVariable long uid,
                                 @RequestParam long bid,
                                 @RequestParam int number) {
        boolean result = userService.addBooksToCart(uid, bid, number);
        return result? Result.success(): Result.error("添加图书失败");
    }

    @GetMapping("/{uid}/cart/clear")
    public Result clearCart(@PathVariable long uid) {
        boolean result = cartService.clearCart(uid);
        return result ? Result.success() : Result.error("清空购物车失败");
    }

    @GetMapping("/{uid}/buy")
    public Result buyBooks(@PathVariable long uid,
                           @RequestParam long bid,
                           @RequestParam int number) {
        boolean result = userService.buyBooks(uid, bid, number);
        return result ? Result.success() : Result.error("购买失败");
    }
}
