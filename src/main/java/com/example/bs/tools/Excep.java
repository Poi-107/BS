package com.example.bs.tools;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

/**
 * 异常处理类
 */
@RestControllerAdvice
public class Excep {
    // 通用异常
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        ex.printStackTrace();
        Map<String, Object> res = new HashMap<>();
        res.put("code", 500);
        res.put("msg", "系统异常");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }

    // 请求方式错误
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethod(HttpRequestMethodNotSupportedException ex) {
        Map<String, Object> res = new HashMap<>();
        res.put("code", 405);
        res.put("msg", "请求方式错误");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(res);
    }

    // 文件过大
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSize(MaxUploadSizeExceededException ex) {
        Map<String, Object> res = new HashMap<>();
        res.put("code", 400);
        res.put("msg", "文件太大（最大10MB）");
        return ResponseEntity.badRequest().body(res);
    }
}
