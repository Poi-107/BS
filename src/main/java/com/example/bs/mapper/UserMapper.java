package com.example.bs.mapper;

import com.example.bs.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 修改用户信息（管理员）
     * @param user
     */
    void upuser(User user);

    /**
     * 修改用户信息（用户）
     * @param user
     */
    void upuser2(User user);

    /**
     * 删除用户
     * @param user
     */
    void deluser(User user);

    /**
     * 查询所有用户
     * @return
     */
    List<User> seluser();

    /**
     * 修改用户头像
     * @param id
     * @param s
     */
    void updateAvatar(@Param("id") Integer id,
                      @Param("avatar_url") String avatarUrl);

    /**
     * 查询用户头像
     * @param id
     * @return
     */
    String selav(Integer id);

    /**
     * 根据id查询用户
     * @param id
     * @return
     */
    Object seluser2(Integer id);
}
