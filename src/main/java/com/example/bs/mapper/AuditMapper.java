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

    /**
     * 添加审核单
     * @param audit
     */
    void addaudit(Audit audit);

    /**
     * 查询status为0的表单
     * @return
     */
    List<Audit> sel0audit();

    /**
     * 审批表单
     * @param audit
     */
    void upaudit(Audit audit);

    /**
     * 根据id查询审核表
     * @param id
     * @return
     */
    Audit selid(Integer id);
}
