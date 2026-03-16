package com.example.bs.controller;

import com.example.bs.entity.Chuku;
import com.example.bs.entity.Result;
import com.example.bs.entity.Ruku;
import com.example.bs.service.ChukuService;
import com.example.bs.service.KucunService;
import com.example.bs.service.RukuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bs")
@Slf4j
public class TongjiController {
    @Autowired
    private RukuService rukuService;
    @Autowired
    private ChukuService chukuService;
    @Autowired
    private KucunService kucunService;


//    查询今日入库（单）
    @GetMapping("/jinruku")
    public Result jinruku(){
        log.info("查询今日入库");
        int ruku=rukuService.jinruku();
        return Result.success(ruku);
    }
//    查询今日出库
    @GetMapping("/jinchuku")
    public Result jinchuku(){
        log.info("查询今日出库");
        int chuku=chukuService.jinchuku();
        return Result.success(chuku);
    }
//    查询今日销售
    @GetMapping("/jinsale")
    public Result jinsale(){
        log.info("查询今日销售");
        int sale=chukuService.jinsale();
        return Result.success(sale);
    }
//    查询昨日销售
//    查询今日采购
    @GetMapping("/jinpur")
    public Result jinpur(){
        log.info("查询今日采购");
        int pur=rukuService.jinpur();
        return Result.success(pur);
    }
//    查询昨日采购
//    查询本月入库
    @GetMapping("/yueruku")
    public Result yueruku(){
        log.info("查询本月入库");
        int ruku=rukuService.yueruku();
        return Result.success(ruku);
    }
//    查询本月出库
    @GetMapping("/yuechuku")
    public Result yuechuku(){
        log.info("查询本月出库");
        int chuku=chukuService.yuechuku();
        return Result.success(chuku);
    }
//    查询本月累计销售
    @GetMapping("/yuesale")
    public Result yuesale(){
        log.info("查询本月累计销售");
        int sale=chukuService.yuesale();
        return Result.success(sale);
    }
//    查询本月累计采购
    @GetMapping("/yuecpur")
    public Result yuecpur(){
        log.info("查询本月累计采购");
        int pur=rukuService.yuecpur();
        return Result.success(pur);
    }
//    查询本年销售
    @GetMapping("/yearsale")
    public Result yearsale(){
        log.info("查询本年销售");
        int sale=chukuService.yearsale();
        return Result.success(sale);
    }
//    查询本年采购
    @GetMapping("/yearpur")
    public Result yearpur(){
        log.info("查询本年采购");
        int pur=rukuService.yearpur();
        return Result.success(pur);
    }


}
