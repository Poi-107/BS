package com.example.bs.controller;

import com.example.bs.aop.AopAnnotation;
import com.example.bs.entity.Result;
import com.example.bs.entity.User;
import com.example.bs.service.UserService;
import com.example.bs.tools.Jwt;
import com.example.bs.tools.interceptor.Per;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/bs")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    // 登录接口
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        log.info("请求登录");
        User u = userService.login(user);
        if (u != null) {
            // 创建 JWT 令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", u.getId());
            claims.put("username", u.getUsername());
            claims.put("per", u.getPer());
            String jwt = Jwt.generateJwt(new HashMap<>(claims));
            // 响应数据
            return Result.success(jwt);
        } else {
            return Result.error("用户名不存在或密码错误！");
        }
    }

    // 注册接口
    @PostMapping("/res")
    public Result res(@RequestBody User user) {
        log.info("请求注册");
        int r = userService.sel(user.getUsername());
        if (r == 0) {
            userService.res(user);
            return Result.success("注册成功！");
        }
        return Result.error("用户已存在！");
    }

    // 修改用户权限（管理员）
    @Per(1)
    @AopAnnotation(target = "用户表", action = "修改用户权限")
    @PostMapping("/upuser")
    public Result upuser(@RequestBody User user) {
        log.info("请求修改用户权限");
        userService.upuser(user);
        return Result.success();
    }

    // 根据 id 获取个人信息
    @GetMapping("/seluser2")
    public Result seluser2(HttpServletRequest request) {
        String token = request.getHeader("bs_token");
        Claims claims = Jwt.parseJwt(token);
        Integer id = Integer.valueOf(claims.get("id").toString());
        return Result.success(userService.seluser2(id));
    }

    // 修改用户信息（用户）
    @AopAnnotation(target = "用户表", action = "修改用户信息")
    @PostMapping("/upuser2")
    public Result upuser2(@RequestBody User user) {
        log.info("请求修改用户信息");
        userService.upuser2(user);
        return Result.success();
    }

    // 删除用户
    @Per(1)
    @AopAnnotation(target = "用户表", action = "删除用户")
    @PostMapping("/deluser")
    public Result deluser(@RequestBody User user) {
        log.info("请求删除用户");
        userService.deluser(user);
        return Result.success("删除成功！");
    }

    // 查询所有用户
    @Per(1)
    @GetMapping("/seluser")
    public Result seluser() {
        log.info("请求查询所有用户");
        List<User> userList = userService.seluser();
        return Result.success(userList);
    }

    // 上传头像
    @PostMapping("/uploadAvatar")
    public Result uploadAvatar(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        // 解析 token，获取用户 id
        String token = request.getHeader("bs_token");
        Claims claims = Jwt.parseJwt(token);
        Integer id = Integer.valueOf(claims.get("id").toString());

        // 读取旧头像
        String oldAvatar = userService.selav(id);

        // 保存文件到本地
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String path = "F:/Idea/bstouxiang/";
        // 检查目录
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File dest = new File(path + filename);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            return Result.error("文件保存失败");
        }

        // 更新数据库中的头像路径
        userService.updateAvatar(id, "/uploads/" + filename);

        // 删除旧头像文件
        if (oldAvatar != null && !oldAvatar.isEmpty()) {
            String oldName = oldAvatar.startsWith("/uploads/")
                    ? oldAvatar.substring("/uploads/".length())
                    : oldAvatar;
            File oldFile = new File(path + oldName);
            if (oldFile.exists()) {
                oldFile.delete();
            }
        }

        // 返回头像 URL
        return Result.success("/uploads/" + filename);
    }

    // 获取头像 url
    @GetMapping("/selav")
    public Result selav(HttpServletRequest request) {
        String token = request.getHeader("bs_token");
        Claims claims = Jwt.parseJwt(token);
        Integer id = Integer.valueOf(claims.get("id").toString());
        String av = userService.selav(id);
        return Result.success(av);
    }
}
