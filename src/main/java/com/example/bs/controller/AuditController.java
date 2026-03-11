package com.example.bs.controller;

import com.example.bs.entity.Audit;
import com.example.bs.entity.Result;
import com.example.bs.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bs")
@Slf4j
public class AuditController {
    @Autowired
    private AuditService auditService;

    //查询审核表
    @GetMapping("/selaudit")
    public Result selaudit(){
        log.info("请求查询审核表");
        List<Audit> auditList = auditService.selaudit();
        return Result.success(auditList);
}
}
