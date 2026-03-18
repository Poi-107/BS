package com.example.bs.tools.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.example.bs.entity.Result;
import com.example.bs.tools.Jwt;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录验证类
 */
//@Slf4j
//@Component
//public class Login implements HandlerInterceptor {
//    @Override //运行前运行 true放行 false拦截
//    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
//        //登录校验
//        //获取url
//        String url = req.getRequestURI().toString();
//        log.info("url:{}", url);
//        //是否登录
//        if (url.contains("/login")) {
//            log.info("登录，放行");
//            return true;
//        }
//        //获取token
//        String jwt = req.getHeader("token");
//        //判断令牌是否存在
//        if (!StringUtils.hasLength(jwt)) {
//            log.info("token不存在");
//            Result error = Result.error("Not_Login");
//            //手动转换json对象
//            String notlogin = JSONObject.toJSONString(error);
//            resp.getWriter().write(notlogin);//相应给浏览器
//            return false;
//        }
//        //解析token令牌，返回错误结果（未登录）
//        try {
//            Jwt.parseJwt(jwt);
//        } catch (Exception e) {//jwt解析失败
//            e.printStackTrace();
//            log.info("token解析失败");
//            Result error = Result.error("Not_Login");
//            //手动转换json对象
//            String notlogin = JSONObject.toJSONString(error);
//            resp.getWriter().write(notlogin);//响应给浏览器
//            return false;
//        }
//        //放行
//        log.info("令牌合法，放行");
//        return true;
//    }
//}
@Slf4j
@Component
public class Login implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {

        String url = req.getRequestURI().toString();
        log.info("url:{}", url);

        // 放行登录
        if (url.contains("/login")) {
            return true;
        }

        // ⭐ 放行静态资源（很重要）
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 获取token
        String jwt = req.getHeader("token");

        if (!StringUtils.hasLength(jwt)) {
            return writeError(resp, "Not_Login", 401);
        }

        Claims claims;
        try {
            claims = Jwt.parseJwt(jwt);
        } catch (Exception e) {
            log.info("token解析失败");
            return writeError(resp, "Not_Login", 401);
        }

        //  获取权限
        Integer per = Integer.valueOf(claims.get("per").toString());
        //  获取当前方法
        HandlerMethod method = (HandlerMethod) handler;
        //  获取注解
        Per rp = method.getMethodAnnotation(Per.class);
        //  权限判断（核心）
        if (rp != null) {
            int needPer = rp.value();

            if (per < needPer) {
                log.info("权限不足，当前per={},需要per={}", per, needPer);
                return writeError(resp, "No_Permission", 403);
            }
        }

        // ⭐ 可选：存用户信息
        req.setAttribute("userId", claims.get("id"));
        req.setAttribute("per", per);

        log.info("令牌合法，权限通过，放行");
        return true;
    }

    // ⭐ 统一返回方法（推荐）
    private boolean writeError(HttpServletResponse resp, String msg, int code) throws Exception {
        resp.setStatus(code);
        Result error = Result.error(msg);
        String json = JSONObject.toJSONString(error);
        resp.getWriter().write(json);
        return false;
    }
}