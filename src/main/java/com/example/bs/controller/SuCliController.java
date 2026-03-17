package com.example.bs.controller;

import com.example.bs.entity.Client;
import com.example.bs.entity.Result;
import com.example.bs.entity.Suppiler;
import com.example.bs.service.SuCliService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 供应商/客户
 */
@RestController
@RequestMapping("/bs")
@Slf4j
public class SuCliController {
    @Autowired
    private SuCliService suclService;
//                          供应商接口
//    查询所有供应商
    @GetMapping("/selsu")
    public Result selsu(){
        log.info("请求查询供应商");
        List<Suppiler> suppiler=suclService.selsu();
        return Result.success(suppiler);
    }
//    获取供应商名称
    @GetMapping("/selsuname")
    public Result selsuname(){
        log.info("请求查询供应商名称");
        List<String> suppiler=suclService.selsuname();
        return Result.success(suppiler);
    }
//    根据name查询供应商信息
    @GetMapping("/selsu2")
    public Result selsu2(String name){
        log.info("请求查询供应商");
        List<Suppiler> suppiler=suclService.selsu2(name);
        return Result.success(suppiler);
    }
//    修改供应商信息
    @PostMapping("/upsu")
    public Result upsu(@RequestBody Suppiler suppiler){
        log.info("请求修改供应商信息");
        suclService.upsu(suppiler);
        return Result.success("修改成功！");
    }
//    删除供应商
    @PostMapping("/delsu")
    public Result delsu(@RequestBody Suppiler suppiler){
        log.info("请求删除供应商");
        suclService.delsu(suppiler);
        return Result.success("删除成功！");
    }
//                          客户接口
//    查询所有客户
    @GetMapping("/selcli")
    public Result selcli(){
        log.info("请求查询客户");
        List<Client> client=suclService.selcli();
        return Result.success(client);
    }
//    获取客户名称
    @GetMapping("/selcliname")
    public Result selcliname(){
        log.info("请求查询客户名称");
        List<String> client=suclService.selcliname();
        return Result.success(client);
    }
//    根据name查询客户
    @GetMapping("/selcli2")
    public Result selcli2(String name){
        log.info("请求查询客户");
        List<Client> client=suclService.selcli2(name);
        return Result.success(client);
    }
//    修改客户信息
    @PostMapping("/upcli")
    public Result upcli(@RequestBody Client client){
        log.info("请求修改客户信息");
        suclService.upcli(client);
        return Result.success("修改成功！");
    }
//    删除客户
    @PostMapping("/delcli")
    public Result delcli(@RequestBody Client client){
        log.info("请求删除客户");
        suclService.delcli(client);
        return Result.success("删除成功！");
    }
}

