package com.example.bs.controller;


import com.example.bs.entity.Log;
import com.example.bs.entity.Result;
import com.example.bs.entity.Xinxi;
import com.example.bs.service.XinxiService;
import com.example.bs.tools.interceptor.Per;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bs")
@Slf4j
public class XinxiController {
    @Autowired
    private XinxiService xinxiService;

//    查询通知信息
    @GetMapping("/selxinxi")
    public Result selxinxi(){
        log.info("请求查询信息");
        List<Xinxi> xinxi=xinxiService.selxinxi();
        return Result.success(xinxi);
    }
//    发布通知信息
    @Per(1)
    @PostMapping("/addxinxi")
    public Result addxinxi(Xinxi xinxi){
        log.info("请求添加信息");
        xinxiService.addxinxi(xinxi);
        return Result.success();
    }
//    删除通知信息
    @Per(1)
    @PostMapping("/delxinxi")
    public Result delxinxi(Integer id){
        log.info("请求删除信息");
        xinxiService.delxinxi(id);
        return Result.success();
    }
//    修改通知信息优先级
    @Per(1)
    @PostMapping("/upxinxi")
    public Result upxinxi(Xinxi xinxi){
        log.info("请求修改信息");
        xinxiService.upxinxi(xinxi);
        return Result.success();
    }

}
