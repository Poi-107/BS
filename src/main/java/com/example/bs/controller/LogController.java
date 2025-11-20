package com.example.bs.controller;

import com.example.bs.entity.Log;
import com.example.bs.entity.Result;
import com.example.bs.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/bs")
public class LogController {
    @Autowired
    private LogService logService;

    //查询日志
    @GetMapping("/sellog")
    public Result selLog(){
         log.info("查询日志请求");
         List<Log> list=logService.sellog();
         return Result.success(list);
    }

    //添加日志
    @PostMapping("/addlog")
    public Result addlog(@RequestBody Log logs){
        log.info("添加日志请求");
        logService.addlog(logs);
        return Result.success();
    }
}
