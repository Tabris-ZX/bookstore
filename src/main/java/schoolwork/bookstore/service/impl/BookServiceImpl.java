package schoolwork.bookstore.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import schoolwork.bookstore.config.PathConfig;
import schoolwork.bookstore.mapper.BookMapper;
import schoolwork.bookstore.model.Book;
import schoolwork.bookstore.pojo.BookQuery;
import schoolwork.bookstore.service.BookService;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    PathConfig pathConfig;
    BookMapper bookMapper;
    public BookServiceImpl(PathConfig pathConfig,BookMapper bookMapper) {
        this.pathConfig = pathConfig;
        this.bookMapper = bookMapper;
    }


    @Override
    public List<Book> getAllBooks() {
        return List.of();
    }

    @Override
    public List<Book> getBooksByCondition(BookQuery bookQuery) {
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        if(bookQuery.getCurPage()>0) wrapper.eq(Book::getBid,bookQuery.getCurPage());
        if (bookQuery.getSize() > 0) wrapper.like(Book::getBid,bookQuery.getSize());
        if (bookQuery.isStock()) wrapper.gt(Book::getStock,0);
        if (bookQuery.getAuthor() != null) wrapper.like(Book::getAuthor,bookQuery.getAuthor());
        if (bookQuery.getPrice()>0) wrapper.le(Book::getPrice,bookQuery.getPrice());
        if (bookQuery.getTags() != null) wrapper.eq(Book::getTags,bookQuery.getTags());
        return bookMapper.selectList(wrapper);
    }

    @Override
    public IPage<Book> pageBooks(int curPage, int size) {
        Page<Book> page = new Page<>(curPage,size);

        return bookMapper.selectPage(page,null);
    }

    @Override
    public IPage<Book> pageBooksByCondition(int curPage, int pageSize, BookQuery bookQuery) {
        return null;
    }

    @Override
    public Book getBookById(long bid) {
        return bookMapper.selectById(bid);
    }

    @Override
    public List<Book> searchBooksByKeyword(String keyword) {
        return bookMapper.selectList(new LambdaQueryWrapper<Book>().like(Book::getTitle,keyword));
    }

    @Override
    public List<Book> searchBooksByCondition(BookQuery bookQuery) {
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        if(bookQuery.getTags()!=null) queryWrapper.eq(Book::getTags,bookQuery.getTags());
        if (bookQuery.getAuthor()!=null) queryWrapper.eq(Book::getAuthor,bookQuery.getAuthor());
        if(bookQuery.getStock()!=null) queryWrapper.eq(Book::getStock,bookQuery.getStock());
        return List.of();
    }

}
