package zx.bookstore.service.impl;

import org.springframework.stereotype.Service;
import zx.bookstore.config.PathConfig;
import zx.bookstore.mapper.BookMapper;
import zx.bookstore.service.BookService;

@Service
public class BookServiceImpl implements BookService {

    PathConfig pathConfig;
    BookMapper bookMapper;
    public BookServiceImpl(PathConfig pathConfig,BookMapper bookMapper) {
        this.pathConfig = pathConfig;
        this.bookMapper = bookMapper;
    }



}
