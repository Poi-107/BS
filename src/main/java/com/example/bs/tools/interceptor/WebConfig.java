package com.example.bs.tools.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * 拦截器类
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private Login login;

    @Value("${upload.dir}")
    private String uploadDir;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(login)
                .addPathPatterns("/**")
                .excludePathPatterns(
                "/bs/login",             //登录接口
                "/bs/res",                //注册接口


                "/uploads/**",       // 图片接口
                "/error",             // 错误页面
                "/favicon.ico",      // 浏览器图标
                "/static/**",         // 静态资源
                "/webjars/**"     // Spring Boot WebJars
        );//要拦截的资源和不拦截的资源
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir);
    }
}
