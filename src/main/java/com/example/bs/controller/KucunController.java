package com.example.bs.controller;

import com.example.bs.entity.Kucun;
import com.example.bs.entity.Result;
import com.example.bs.service.KucunService;
import com.example.bs.tools.interceptor.Per;
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
//    根据code查询物品，得到leibie和name
    @GetMapping("/selcode")
    public Result selcode(@RequestParam String code){
        log.info("请求查询库存");
        Kucun kucun=kucunService.selcode(code);
        return Result.success(kucun);
    }
//    分类查询

    @GetMapping("/selkucun1")
    public Result selleibie(@RequestParam String leibie){
        log.info("请求分类查询库存");
        List<Kucun> kucun=kucunService.selleibie(leibie);
        return Result.success(kucun);
    }
//    获取所有类别

    @GetMapping("/selleibie2")
    public Result selleibie() {
        log.info("请求获取所有类别");
        List<String> kucunList = kucunService.selleibie2();
        return Result.success(kucunList);
    }
//    根据id修改库存表内容
    @Per(1)
    @PostMapping("/upkucun")
    public Result upkucun(@RequestBody Kucun kucun){
        log.info("请求修改库存表内容");
        kucunService.upkucun2(kucun);
        return Result.success();
    }


}

