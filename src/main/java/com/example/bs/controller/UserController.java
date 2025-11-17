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

    @PostMapping("/login")
    public Result  login(@RequestBody User user){
        log.info("login");
        User u=userService.login(user);
        if(u==null){
            return Result.error("登陆失败");
        }
        return Result.success("登陆成功");


    }
}
