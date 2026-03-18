package com.example.bs.controller;

import com.example.bs.aop.AopAnnotation;
import com.example.bs.entity.Chuku;
import com.example.bs.entity.Result;
import com.example.bs.entity.Ruku;
import com.example.bs.service.ChukuService;
import com.example.bs.tools.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/bs")
@Slf4j
public class ChukuController {
    @Autowired
    private ChukuService chukuService;

    //查询出库单
    @GetMapping("/selchuku")
    public Result selchuku(){
        log.info("请求查询出库单");
        List<Chuku> chuku=chukuService.selchuku();
        return Result.success(chuku);
    }
//    获取所有类别
    @GetMapping("/selleibie1")
    public Result selleibie(){
        log.info("请求获取所有类别");
        List<String> chuku=chukuService.selleibie1();
        return Result.success(chuku);
    }
//    分类查询
    @GetMapping("/selchuku1")
    public Result selchuku1(@RequestParam String  leibie){
        log.info("请求分类查询出库单");
        List<Chuku> chuku=chukuService.selchuku1(leibie);
        return Result.success(chuku);
    }
    //出库
    @AopAnnotation(target = "出库",action = "进行出库")
    @RequestMapping("/addchuku")
    public Result chuku(@RequestBody Chuku chuku){
        log.info("请求出库");
        String user = UserContext.getCurrentUserName();
        chuku.setUser(user);
        chuku.setCktime(LocalDateTime.now());
        chukuService.addchuku(chuku);
        return Result.error("出库成功！");
    }

}
