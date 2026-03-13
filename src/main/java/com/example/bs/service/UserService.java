package com.example.bs.service;

import com.example.bs.mapper.UserMapper;
import com.example.bs.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * 修改用户信息（管理员）
     * @param user
     */
    public void upuser(User user) {
        userMapper.upuser(user);
    }

    /**
     * 修改用户信息（用户）
     * @param user
     */
    public void upuser2(User user) {
        userMapper.upuser2(user);
    }

    /**
     * 删除用户
     * @param user
     */
    public void deluser(User user) {
        userMapper.deluser(user);
    }

    /**
     * 查询所有用户
     * @return
     */
    public List<User> seluser() {
        return userMapper.seluser();
    }

    /**
     * 修改头像
     * @param id
     * @param s
     */
    public void updateAvatar(Integer id, String s) {
        userMapper.updateAvatar(id, s);
    }

    /**
     * 获取头像
     * @param id
     * @return
     */
    public String selav(Integer id) {
        return userMapper.selav(id);
    }
}
