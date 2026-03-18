package com.example.bs.controller;

import com.example.bs.aop.AopAnnotation;
import com.example.bs.entity.Audit;
import com.example.bs.entity.Kucun;
import com.example.bs.entity.Result;
import com.example.bs.service.AuditService;
import com.example.bs.tools.UserContext;
import com.example.bs.tools.interceptor.Per;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/bs")
@Slf4j
public class AuditController {
    @Autowired
    private AuditService auditService;

    @Per(1)
    //查询审核表
    @GetMapping("/selaudit")
    public Result selaudit() {
        log.info("请求查询审核表");
        List<Audit> auditList = auditService.selaudit();
        return Result.success(auditList);
    }
    //根据id查询审核表
    @Per(1)
    @GetMapping("/selid")
    public Result selidaudit(@RequestBody Audit a) {
        log.info("根据id查询审核表");
        Audit audit=auditService.selid(a.getId());
        return Result.success(audit);
    }
//    获取所有类别
    @Per(1)
    @GetMapping("/selleibie")
    public Result selleibie() {
        log.info("请求获取所有类别");
        List<String> auditList = auditService.selleibie();
        return Result.success(auditList);
    }
//    分类查询
    @Per(1)
    @GetMapping("/selaudit1")
    public Result selaudit1(@RequestParam String leibie) {
        log.info("请求分类查询审核表");
        List<Audit> auditList = auditService.selaudit1(leibie);
        return Result.success(auditList);
    }

    //待审核
    @Per(1)
    @GetMapping("/sel0audit")
    public Result pendingAudit() {
        log.info("请求查询待审核列表");
        List<Audit> auditList = auditService.sel0audit();
        return Result.success(auditList);
    }
    //审批表单
    @Per(1)
    @AopAnnotation(target = "审批",action = "进行审批")
    @PostMapping("/upaudit")
    public Result upaudit(@RequestBody Audit audit) {
        log.info("请求审批表单");
        String reviewer = UserContext.getCurrentUserName();
        audit.setReviewer(reviewer);
        audit.setReviewertime(LocalDateTime.now());
        auditService.upaudit(audit);
        return Result.success();
    }
}