package com.example.bs.controller;

import com.example.bs.entity.Result;
import com.example.bs.entity.Ruku;
import com.example.bs.service.RukuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/bs")
@Slf4j
public class RukuController {
    @Autowired
    private RukuService rukuService;

//    查询入库单
    @GetMapping("/selruku")
    public Result ruku(){
        log.info("请求查询入库单");
        List<Ruku> ruku=rukuService.selruku();
        return Result.success(ruku);
    }

//    添加入库
    @PostMapping("/addruku")
    public Result addruku(@RequestBody Ruku ruku){
        log.info("请求添加入库单");
        ruku.setRktime(LocalDateTime.now());
        rukuService.addruku(ruku);
        return Result.success("添加成功！");
    }
}
            