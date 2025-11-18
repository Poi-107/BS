package com.example.bs.controller;

import com.example.bs.pojo.Result;
import com.example.bs.pojo.User;
import com.example.bs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        if(u==null){
            return Result.error("登陆失败！");
        }
        return Result.success("登陆成功！");
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
