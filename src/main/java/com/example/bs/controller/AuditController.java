package com.example.bs.controller;

import com.example.bs.entity.Audit;
import com.example.bs.entity.Kucun;
import com.example.bs.entity.Result;
import com.example.bs.service.AuditService;
import com.example.bs.tools.UserContext;
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

    //查询审核表
    @GetMapping("/selaudit")
    public Result selaudit() {
        log.info("请求查询审核表");
        List<Audit> auditList = auditService.selaudit();
        return Result.success(auditList);
    }
    //根据id查询审核表
    @GetMapping("/selid")
    public Result selidaudit(@RequestBody Audit a) {
        log.info("根据id查询审核表");
        Audit audit=auditService.selid(a.getId());
        return Result.success(audit);
    }

    //待审核
    @GetMapping("/sel0audit")
    public Result pendingAudit() {
        log.info("请求查询待审核列表");
        List<Audit> auditList = auditService.sel0audit();
        return Result.success(auditList);
    }
    //审批表单
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