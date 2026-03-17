package com.example.bs.tools;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

/**
 * JWT
 */
public class Jwt {
    //密钥
    private static final String SECRET_KEY = "bsqwerdfvb";
    //一天时间
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;//

    //生成JwtToken
    public static String generateJwt(Map<String, Object> claims) {

        String jwt = Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        return jwt;
    }

    //解析JwtToken
    public static Claims parseJwt(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }
}