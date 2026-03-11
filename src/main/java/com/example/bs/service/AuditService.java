package com.example.bs.service;

import com.example.bs.entity.Audit;
import com.example.bs.mapper.AuditMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {
    @Autowired
    private AuditMapper auditMapper;


    /**
     * 查询审核表
     * @return
     */
    public List<Audit> selaudit() {
        return auditMapper.selaudit();
    }
}
