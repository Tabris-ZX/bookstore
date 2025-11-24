package zx.bookstore.service.impl;

import org.springframework.stereotype.Service;
import zx.bookstore.mapper.UserMapper;
import zx.bookstore.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    UserMapper userMapper;
    public  UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

}
