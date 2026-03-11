package com.example.bs.mapper;

import com.example.bs.entity.Audit;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuditMapper {


    /**
     * 查询审核表
     * @return
     */
    List<Audit> selaudit();
}
