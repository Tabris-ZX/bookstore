package schoolwork.bookstore.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import schoolwork.bookstore.dto.LoginRequest;
import schoolwork.bookstore.model.User;
import schoolwork.bookstore.dto.Result;
import schoolwork.bookstore.model.UserAddr;
import schoolwork.bookstore.service.UserService;
import schoolwork.bookstore.util.JwtUtil;

import java.io.IOException;

@Slf4j
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

    @PostMapping("/chat/ai")
    public Object getRecommendedBooks(HttpServletRequest request,
                                      @RequestParam(defaultValue = "false") boolean stream,
                                      @RequestBody String wanting) {
        if (stream) {
            SseEmitter emitter = new SseEmitter(60000L);
            new Thread(() -> {
                try {
                    userService.getAlRecommendationStream(wanting, JwtUtil.getUid(request), chunk -> {
                        try {
                            emitter.send(SseEmitter.event()
                                    .data(chunk)
                                    .name("message"));
                        } catch (IOException e) {
                            log.error("发送流式数据失败", e);
                            emitter.completeWithError(e);
                        }
                    });
                    
                    emitter.send(SseEmitter.event().data("[DONE]").name("done"));
                    emitter.complete();
                } catch (Exception e) {
                    log.error("流式处理失败", e);
                    try {
                        emitter.send(SseEmitter.event().data("错误: " + e.getMessage()).name("error"));
                        emitter.completeWithError(e);
                    } catch (IOException ioException) {
                        emitter.completeWithError(ioException);
                    }
                }
            }).start();
            return emitter;
        } else {
            // 非流式输出模式
            return userService.getAlRecommendation(wanting, JwtUtil.getUid(request))
                    .thenApply(Result::success);
        }
    }

}
