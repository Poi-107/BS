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

    /**
     * 注册接口1
     * 查看是否存在相同账户
     * @param username
     * @return
     */
    public int sel(String username) {
        return userMapper.sel(username);
    }

    /**
     * 注册接口2
     * 添加用户
     * @param user
     */
    public void res(User user) {
        userMapper.res(user);
    }
}
