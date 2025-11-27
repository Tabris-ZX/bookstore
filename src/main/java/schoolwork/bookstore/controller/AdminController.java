package schoolwork.bookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import schoolwork.bookstore.model.Book;
import schoolwork.bookstore.model.User;
import schoolwork.bookstore.pojo.Result;
import schoolwork.bookstore.service.AdminService;
import schoolwork.bookstore.service.impl.AdminServiceImpl;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    AdminService adminService;
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/user/create")
    public Result createUser(@RequestBody User user) {
        return Result.success(user);
    }

    @DeleteMapping("/user/delete/{uid}")
    public Result deleteUser(@PathVariable long uid) {
        return adminService.removeUser(uid)? Result.success("User deleted") : Result.error("删除用户失败");
    }

    @PostMapping("/book/add")
    public Result addBook(@RequestBody Book book) {
        return adminService.addBook(book) ? Result.success("Book added") : Result.error("添加图书失败");
    }

    @PutMapping("/book/update")
    public Result updateBook(@RequestBody Book book) {
        return adminService.updateBookInfo(book) ? Result.success("Book updated") : Result.error("更新图书信息失败");
    }

}

