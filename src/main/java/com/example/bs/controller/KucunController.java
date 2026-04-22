package com.example.bs.controller;

import com.example.bs.entity.Kucun;
import com.example.bs.entity.Result;
import com.example.bs.service.KucunService;
import com.example.bs.tools.interceptor.Per;
import com.example.bs.tools.yujing.InventoryWarningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bs")
@Slf4j
public class KucunController {
    @Autowired
    private KucunService kucunService;

    @Autowired
    private InventoryWarningService inventoryWarningService;

    @GetMapping("/selkucun")
    public Result selkucun() {
        log.info("请求查询库存");
        List<Kucun> kucun = kucunService.selkucun();
        return Result.success(kucun);
    }


    @GetMapping("/selname")
    public Result selname(@RequestParam String name) {
        log.info("请求按名称查询库存");
        Kucun kucun = kucunService.selname(name);
        return Result.success(kucun);

    }

    @GetMapping("/selcode")
    public Result selcode(@RequestParam String code) {
        log.info("请求按条码查询库存");
        Kucun kucun = kucunService.selcode(code);
        return Result.success(kucun);
    }

    @GetMapping("/selkucun1")
    public Result selleibie(@RequestParam String leibie) {
        log.info("请求分类查询库存");
        List<Kucun> kucun = kucunService.selleibie(leibie);
        return Result.success(kucun);
    }

    @GetMapping("/selleibie2")
    public Result selleibie() {
        log.info("请求获取所有类别");
        List<String> kucunList = kucunService.selleibie2();
        return Result.success(kucunList);
    }

    @Per(1)
    @PostMapping("/upkucun")
    public Result upkucun(@RequestBody Kucun kucun) {
        log.info("请求修改库存");
        kucunService.upkucun2(kucun);
        inventoryWarningService.recomputeSafeStock(false); // 重新计算但不发送通知
        return Result.success();
    }

    //安全库存预警功能模块
    @GetMapping("/warning/config")
    public Result warningConfig() {
        Map<String, Object> data = new HashMap<>();
        data.put("z", inventoryWarningService.getZ());
        data.put("lastCalcDate", inventoryWarningService.getLastCalcDate());
        return Result.success(data);
    }

    @Per(1)
    @PostMapping("/warning/config")
    public Result updateWarningConfig(@RequestBody Map<String, Object> body) {
        if (body == null || !body.containsKey("z")) {
            return Result.error("z不能为空");
        }
        double z;
        try {
            z = Double.parseDouble(String.valueOf(body.get("z")));
        } catch (Exception e) {
            return Result.error("z格式不正确");
        }
        if (z <= 0) {
            return Result.error("z必须大于0");
        }
        inventoryWarningService.setZ(z);
        inventoryWarningService.recomputeSafeStock(false); // 重新计算但不发送通知
        return Result.success();
    }
}
