package zx.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zx.bookstore.model.User;
import zx.bookstore.pojo.Result;
import zx.bookstore.service.UserService;

@Controller
@RequestMapping("/api/user")
public class UserController {

    UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{uid}")
    public Result getUser(@PathVariable Integer uid) {
        return null;
    }

    @PutMapping("/{uid}")
    public Result updateUser(@PathVariable Integer uid, @RequestBody User user) {
        return null;
    }

    @DeleteMapping("/{uid}")
    public Result deleteUser(@PathVariable Integer uid) {
        return null;
    }


}
