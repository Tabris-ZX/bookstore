package schoolwork.bookstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import schoolwork.bookstore.mapper.BookMapper;
import schoolwork.bookstore.mapper.UserMapper;
import schoolwork.bookstore.model.Book;
import schoolwork.bookstore.model.User;
import schoolwork.bookstore.service.AdminService;
import schoolwork.bookstore.util.Build;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    UserMapper userMapper;
    BookMapper bookMapper;
    HttpServletRequest request;

    public AdminServiceImpl(UserMapper userMapper, BookMapper bookMapper, HttpServletRequest request) {
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
        this.request = request;
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.selectList(null);
    }

    @Override
    public IPage<User> pageUsers(int curPage, int pageSize) {
        Page<User> page = new Page<>(curPage, pageSize);
        return userMapper.selectPage(page, null);
    }

    @Override
    public boolean banUser(long uid) {
       return userMapper.banUser(uid);
    }

    @Override
    public boolean unBanUser(long uid) {
        return userMapper.unbanUser(uid);
    }

    @Override
    public boolean addUser(User user) {
        return userMapper.insert(user)==1;
    }

    public boolean addUser(String username, String password) {
        User user = new  User(username, password);
        return userMapper.insert(user) > 0;
    }

    public boolean removeUser(long uid) {
        return userMapper.delete(new LambdaQueryWrapper<User>().eq(User::getUid, uid)) > 0;
    }

    public boolean addBook(Book book) {
        long bid=Build.buildBid();
        while (bookMapper.selectOne(new LambdaQueryWrapper<Book>().eq(Book::getBid,bid))!=null){
            bid= Build.buildBid();
        }
        book.setBid(bid);
        return bookMapper.insert(book) > 0;
    }

    @Override
    public boolean updateBookInfo(Book book) {
        return bookMapper.update(book,new LambdaUpdateWrapper<Book>().eq(Book::getBid,book.getBid())) > 0;
    }

    @Override
    public boolean updateBookStock(long bid, int curStock) {
        return bookMapper.updateBookStock(bid, curStock);
    }

    public boolean removeBook(long bid) {
        return bookMapper.deleteById(bid) > 0;
    }

}
