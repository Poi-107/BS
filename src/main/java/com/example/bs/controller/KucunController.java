package com.example.bs.controller;

import com.example.bs.entity.Kucun;
import com.example.bs.entity.Result;
import com.example.bs.service.KucunService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bs")
@Slf4j
public class KucunController {
    @Autowired
    private KucunService kucunService;

//    查询库存
    @GetMapping("/selkucun")
    public Result selkucun(){
        log.info("请求查询库存");
        List<Kucun> kucun=kucunService.selkucun();
        return Result.success(kucun);
    }

    //根据name查询库存
    @GetMapping("/selname")
    public Result selname(@RequestBody Kucun k){
        log.info("请求查询库存");
        Kucun kucun=kucunService.selname(k.getName());
        return Result.success(kucun);
    }


}

