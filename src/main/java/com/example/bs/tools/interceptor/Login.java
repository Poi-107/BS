package com.example.bs.tools.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.example.bs.entity.Result;
import com.example.bs.tools.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class Login implements HandlerInterceptor {
    @Override //运行前运行 true放行 false拦截
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        //登录校验
        //获取url
        String url = req.getRequestURI().toString();
        log.info("url:{}", url);
        //是否登录
        if (url.contains("/login")) {
            log.info("登录，放行");
            return true;
        }
        //获取token
        String jwt = req.getHeader("token");
        //判断令牌是否存在
        if (!StringUtils.hasLength(jwt)) {
            log.info("token不存在");
            Result error = Result.error("Not_Login");
            //手动转换json对象
            String notlogin = JSONObject.toJSONString(error);
            resp.getWriter().write(notlogin);//相应给浏览器
            return false;
        }
        //解析token令牌，返回错误结果（未登录）
        try {
            Jwt.parseJwt(jwt);
        } catch (Exception e) {//jwt解析失败
            e.printStackTrace();
            log.info("token解析失败");
            Result error = Result.error("Not_Login");
            //手动转换json对象
            String notlogin = JSONObject.toJSONString(error);
            resp.getWriter().write(notlogin);//响应给浏览器
            return false;
        }
        //放行
        log.info("令牌合法，放行");
        return true;
    }
}