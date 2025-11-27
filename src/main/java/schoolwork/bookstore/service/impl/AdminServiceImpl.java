package schoolwork.bookstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import schoolwork.bookstore.mapper.BookMapper;
import schoolwork.bookstore.mapper.UserMapper;
import schoolwork.bookstore.model.Book;
import schoolwork.bookstore.model.User;
import schoolwork.bookstore.service.AdminService;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    UserMapper userMapper;
    BookMapper bookMapper;
    public AdminServiceImpl(UserMapper userMapper, BookMapper bookMapper) {
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
    }


    @Override
    public List<User> getAllUsers() {
        return List.of();
    }

    @Override
    public boolean banUser(long uid) {
        return false;
    }

    @Override
    public boolean unBanUser(long uid) {
        return false;
    }

    public boolean addUser(String username, String password) {
        User user = new  User(username, password);
        return userMapper.insert(user) > 0;
    }



    public boolean removeUser(long uid) {
        return userMapper.delete(new LambdaQueryWrapper<User>().eq(User::getUid, uid)) > 0;
    }


    public boolean addBook(Book book) {
        return bookMapper.insert(book) > 0;
    }

    @Override
    public boolean updateBookInfo(Book book) {
        return bookMapper.updateById(book) > 0;
    }

    @Override
    public boolean updateBookStock(long bid, int curStock) {
        return bookMapper.updateBookStock(bid, curStock);
    }

    public boolean removeBook(long bid) {
        return bookMapper.deleteById(bid) > 0;
    }



}
