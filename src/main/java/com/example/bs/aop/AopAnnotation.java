package com.example.bs.aop;

import java.lang.annotation.*;

@Target({ElementType.METHOD})//  注解作用在方法
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AopAnnotation {

    String target(); // 操作对象（例如“员工表”）
    String action(); // 操作行为（例如“新增员工”）

}
