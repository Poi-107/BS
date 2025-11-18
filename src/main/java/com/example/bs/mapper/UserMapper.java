package com.example.bs.mapper;

import com.example.bs.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    /**
     * 登录接口
     * @param user
     * @return
     */
    User login(User user);

    /**
     * 注册接口1
     * 查看是否存在相同账户
     * @param username
     * @return
     */
    Integer sel(String username);

    /**
     * 注册接口2
     * 添加用户
     * @param user
     */
    void res(User user);
}
