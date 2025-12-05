package schoolwork.bookstore.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import schoolwork.bookstore.dto.LoginRequest;
import schoolwork.bookstore.model.User;
import schoolwork.bookstore.dto.Result;
import schoolwork.bookstore.model.UserAddr;
import schoolwork.bookstore.service.UserService;
import schoolwork.bookstore.util.JwtUtil;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user){
        boolean result = userService.register(user.getUsername(), user.getPassword());
        return result ? Result.success() : Result.error("注册失败");
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginRequest loginUser){
        User cur;
        switch (loginUser.getType()){
            case "username" ->
                    cur = userService.loginByUsername(loginUser.getUsername(), loginUser.getPassword());
            case "uid" ->
                    cur = userService.loginByUid(loginUser.getUid(), loginUser.getPassword());
            case "phone" ->
            {
                return Result.success("手机号登录功能维护中...");
            }
            default -> {
                return Result.error("不支持的登录类型");
            }
        }
        if (cur != null){
            String token = JwtUtil.getJwt(cur);
            return  Result.success(token);
        }else {
            return Result.error("用户不存在或密码错误");
        }
    }

    @GetMapping("/{uid}")
    public Result getUser(@PathVariable long uid) {
        return Result.success(userService.getUserByUid(uid));
    }

    /**
     * 获取用户地址信息
     */
    @GetMapping("/addr")
    public Result getUserAddr(HttpServletRequest request) {
        UserAddr userAddr = userService.getUserAddr(JwtUtil.getUid(request));
        return Result.success(userAddr);
    }

    // @GetMapping("/profile")
    // public Result getProfile(HttpServletRequest request) {
    //     User user = userService.getUserByUid(JwtUtil.getUid(request));
    //     return Result.success(user);
    // }

    @PutMapping("/addr")
    public Result updateUserAddr(HttpServletRequest request,
                                 @RequestBody UserAddr userAddr) {
        if((JwtUtil.getUid(request)!= userAddr.getUid())){
            return Result.error("只能更新自己的地址信息");
        }
        boolean result = userService.updateInfo(userAddr);
        return result ? Result.success() : Result.error("更新用户地址信息失败");
    }   

    @PutMapping("/auth")
    public Result changeAuth(HttpServletRequest request,
                             @RequestParam String type,
                             @RequestBody String content) {
        boolean result = userService.changeAuth(JwtUtil.getUid(request),type, content);
        return result ? Result.success() : Result.error("修改认证信息失败");
    }

    @GetMapping("/buy")
    public Result buyBooks(HttpServletRequest request,
                           @RequestParam long bid,
                           @RequestParam int number) {
        boolean result = userService.buyBooks(JwtUtil.getUid(request),bid, number);
        return result ? Result.success() : Result.error("购买失败");
    }

}
