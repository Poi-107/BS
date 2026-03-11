package com.example.bs.tools;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 日志表
 * 从 Token 中解析用户信息
 */
@Component
public class UserContext {
    //通过token解析id
    public static String getCurrentUserId() {
        String token = getToken();
        if (StringUtils.hasText(token)) {
            try {
                Claims claims = Jwt.parseJwt(token);
                Object idObj = claims.get("id");
                return idObj != null ? idObj.toString() : null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
    //通过token解析username
    public static String getCurrentUserName() {
        String token = getToken();
        if (StringUtils.hasText(token)) {
            try {
                Claims claims = Jwt.parseJwt(token);
                return claims.get("username", String.class);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
    //获取token
    private static String getToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) return null;
        HttpServletRequest request = attributes.getRequest();
        return request.getHeader("token");
    }
}