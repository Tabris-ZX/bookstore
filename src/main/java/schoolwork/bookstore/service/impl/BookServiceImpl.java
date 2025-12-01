package schoolwork.bookstore.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import schoolwork.bookstore.config.PathConfig;
import schoolwork.bookstore.dto.AIData;
import schoolwork.bookstore.mapper.BookMapper;
import schoolwork.bookstore.model.Book;
import schoolwork.bookstore.service.BookService;
import schoolwork.bookstore.util.AIUtil;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class BookServiceImpl implements BookService {

    PathConfig pathConfig;
    BookMapper bookMapper;
    AIUtil aiUtil;
    public BookServiceImpl(PathConfig pathConfig, BookMapper bookMapper, AIUtil aiUtil) {
        this.pathConfig = pathConfig;
        this.bookMapper = bookMapper;
        this.aiUtil = aiUtil;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookMapper.selectList(null) ;
    }

    @Override
    public List<Book> getBooksByCondition(String keyword,
                                          String author,
                                          String tags,
                                          Boolean isStock) {

        return bookMapper.selectList(conditions(keyword, author, tags, isStock));
    }

    @Override
    public IPage<Book> pageBooks(int curPage, int size) {
        Page<Book> page = new Page<>(curPage,size);

        return bookMapper.selectPage(page,null);
    }

    @Override
    public IPage<Book> pageBooksByCondition(int curPage,
                                            int pageSize,
                                            String keyword,
                                            String author,
                                            String tags,
                                            Boolean isStock) {

        Page<Book> page = new Page<>(curPage,pageSize);
        return bookMapper.selectPage(page,conditions(keyword, author, tags, isStock));
    }



    @Override
    public Book getBookByBid(long bid) {
        return bookMapper.selectOne(new LambdaQueryWrapper<Book>().eq(Book::getBid,bid));
    }

    @Override
    public CompletableFuture<String> getAlRecommendation(String wanting) {
        List<AIData> books = bookMapper.getBookInfoForAI();
        String bookInfo = JSON.toJSONString(books);
        return aiUtil.getGeminiRecommendation(wanting,bookInfo);
    }

//    @Override
//    public List<Book> searchBooksByCondition(BookQuery bookQuery) {
//        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
//        if(bookQuery.getTags()!=null) queryWrapper.eq(Book::getTags,bookQuery.getTags());
//        if (bookQuery.getAuthor()!=null) queryWrapper.eq(Book::getAuthor,bookQuery.getAuthor());
////        if(bookQuery.isStock()) queryWrapper.eq(Book::getStock,bookQuery.getStock());
//        return List.of();
//    }

    private LambdaQueryWrapper<Book> conditions(String title, String author, String tags, Boolean isStock) {
        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        if(title!=null && !title.isEmpty()) wrapper.like(Book::getTitle,title);
        if(author!=null && !author.isEmpty()) wrapper.like(Book::getAuthor,author);
        if(tags!=null && !tags.isEmpty()) wrapper.like(Book::getTags,tags);
        if(isStock!=null && isStock) wrapper.gt(Book::getStock,0);
        return wrapper;
    }
}
