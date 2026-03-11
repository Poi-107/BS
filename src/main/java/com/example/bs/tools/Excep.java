package com.example.bs.tools;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 异常处理类
 */
@RestControllerAdvice
public class Excep {

    // 捕获所有运行时异常（如数据库唯一约束冲突等）
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {

        ex.printStackTrace();   // 打印异常
        Map<String, Object> response = new HashMap<>();
        response.put("code", 500);
        response.put("msg", "服务器错误！"+ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    // 处理请求方法不支持的异常
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleIllegalArgumentException(HttpRequestMethodNotSupportedException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 405);
        response.put("msg", "请求方式错误！ ");
        return ResponseEntity.badRequest().body(response);
    }


}
