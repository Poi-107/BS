package com.example.bs.controller;

import com.example.bs.entity.Chuku;
import com.example.bs.entity.Result;
import com.example.bs.service.ChukuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/bs")
@Slf4j
public class ChukuController {
    @Autowired
    private ChukuService chukuService;

    //出库
    @RequestMapping("/chuku")
    public Result chuku(@RequestBody Chuku chuku){
        log.info("请求出库");
        chuku.setCktime(LocalDateTime.now());
        int i = chukuService.chuku(chuku);
        if (i==1){
            return Result.success("出库成功");
        }
        return Result.error("出库失败");
    }

}
