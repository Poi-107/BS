package com.example.bs.controller;

import com.example.bs.entity.Result;
import com.example.bs.entity.User;
import com.example.bs.service.UserService;
import com.example.bs.tools.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/bs")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

//    登录接口
    @PostMapping("/login")
    public Result  login(@RequestBody User user){
        log.info("请求登录");
        User u=userService.login(user);
        if (u != null) {
            //创建JwT令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", u.getId());
            claims.put("username", u.getUsername());
            String jwt = Jwt.generateJwt(new HashMap<>(claims));
            //响应数据
            return Result.success(jwt);
//            return Result.success("登录成功！");
        } else
            return Result.error("用户名不存在或密码错误！");
    }

//    注册接口
    @PostMapping("/res")
    public Result res(@RequestBody User user){
        log.info("请求注册");
        int r=userService.sel(user.getUsername());
        if(r==0){
            userService.res(user);
            return Result.success("注册成功！");
        }
        return Result.error("用户已存在！");
    }


}
