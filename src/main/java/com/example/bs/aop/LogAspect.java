package com.example.bs.aop;

import com.example.bs.entity.Log;
import com.example.bs.service.LogService;
import com.example.bs.tools.UserContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.aspectj.lang.reflect.MethodSignature;
import java.lang.reflect.Method;
import java.time.LocalDateTime;


@Component
@Aspect  //定义了通知和切点的关系
public class LogAspect {
    @Autowired
    private LogService logService;

    @Pointcut("@annotation(com.example.bs.aop.AopAnnotation)")
    public void pt () {
    }
    // 环绕通知：执行目标方法前后插入日志操作
    @Around("pt()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. 获取注解信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        AopAnnotation annotation = method.getAnnotation(AopAnnotation.class);

        // 3. 获取当前用户信息（从 token 中解析）
        String username = UserContext.getCurrentUserName();
        String userIdStr = UserContext.getCurrentUserId();

//        System.out.println(username);
//        System.out.println(userIdStr);

        Integer userId = null;
        if (userIdStr != null) {
            try {
                userId = Integer.valueOf(userIdStr);
            } catch (NumberFormatException e) {
                userId = null;
            }
        }

        // 4. 构造日志对象
        Log log = new Log();
        log.setName(username);
        log.setEmpid(userId);
        log.setTime(LocalDateTime.now());

        if (annotation != null) {
            log.setTarget(annotation.target());
            log.setAction(annotation.action());
        }

        // 5. 保存日志到数据库
        logService.addlog(log);

        // 6. 执行原方法
        return joinPoint.proceed();
    }
}