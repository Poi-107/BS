package com.example.bs.service;

import com.example.bs.mapper.UserMapper;
import com.example.bs.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper  userMapper;

    /**
     * 登录接口
     * @param user
     * @return
     */
    public User login(User user) {
        return userMapper.login(user);
    }
}
