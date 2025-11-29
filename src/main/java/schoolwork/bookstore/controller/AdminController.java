package schoolwork.bookstore.controller;

import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;
import schoolwork.bookstore.model.Book;
import schoolwork.bookstore.model.User;
import schoolwork.bookstore.dto.Result;
import schoolwork.bookstore.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/user")
    public Result createUser(@RequestBody User user) {
        return adminService.addUser(user)? Result.success(): Result.error("创建用户失败");
    }

    @DeleteMapping("/user/{uid}")
    public Result deleteUser(@PathVariable long uid) {
        return adminService.removeUser(uid)? Result.success("用户删除成功") : Result.error("删除用户失败");
    }

    @PostMapping("/book")
    public Result addBook(@RequestBody Book book) {
        return adminService.addBook(book) ? Result.success("图书添加成功") : Result.error("添加图书失败");
    }

    @PutMapping("/book/{bid}")
    public Result updateBook(@RequestBody Book book) {
        return adminService.updateBookInfo(book) ? Result.success("图书更新成功") : Result.error("更新图书信息失败");
    }

    @GetMapping("/users/list")
    public Result getAllUsers() {
        return Result.success(adminService.getAllUsers());
    }

    @GetMapping("/users")
    public Result pageUsers(@Param("page") Integer page,
                            @Param("size") Integer size) {
        return Result.success(adminService.getAllUsers());
    }


    @GetMapping("/ban/{uid}")
    public Result banUser(@PathVariable long uid) {
        return adminService.banUser(uid) ? Result.success("用户封禁成功") : Result.error("封禁用户失败");
    }
    @GetMapping("/unban/{uid}")
    public Result unBanUser(@PathVariable long uid) {
        return adminService.unBanUser(uid) ? Result.success("用户解封成功") : Result.error("解封用户失败");
    }

}

