package com.example.bs.service;

import com.example.bs.entity.Audit;
import com.example.bs.entity.Chuku;
import com.example.bs.entity.Kucun;
import com.example.bs.entity.Ruku;
import com.example.bs.mapper.AuditMapper;
import com.example.bs.mapper.ChukuMapper;
import com.example.bs.mapper.KucunMapper;
import com.example.bs.mapper.RukuMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@Service
public class AuditService {
    @Autowired
    private AuditMapper auditMapper;
    @Autowired
    private RukuMapper rukuMapper;
    @Autowired
    private KucunMapper kucunMapper;
    @Autowired
    private ChukuMapper chukuMapper;


    /**
     * 查询审核表
     * @return
     */
    public List<Audit> selaudit() {
        return auditMapper.selaudit();
    }

    /**
     * 查询status为0的表单
     * @return
     */
    public List<Audit> sel0audit() {
        return auditMapper.sel0audit();
    }

    /**
     * 审批表单
     * @param audit
     */
    @Transactional
    public void upaudit(Audit audit) {
        auditMapper.upaudit(audit);
        // 如果审核通过
        if(audit.getStatus() == 1) {
            // 1 写入入库表
            Audit dbAudit = auditMapper.selid(audit.getId());
            if ("IN".equals(dbAudit.getType())) {
                Ruku ruku = new Ruku();
                ruku.setName(dbAudit.getName());
                ruku.setSupplier(dbAudit.getParther());
                ruku.setPrice(dbAudit.getPrice());
                ruku.setQuantity(dbAudit.getQuantity());
                ruku.setMoney(dbAudit.getMoney());
                ruku.setUser(dbAudit.getUser());
                ruku.setRktime(LocalDateTime.now());
                rukuMapper.addruku(ruku);
                // 2 更新库存
                Kucun kucun = kucunMapper.selname(dbAudit.getName());
                if (kucun == null) {
                    kucunMapper.addkucun(ruku);
                } else {
                    kucun.setQuantity(kucun.getQuantity() + dbAudit.getQuantity());
                    kucunMapper.upkucun(kucun);
                }
            }
            if ("OUT".equals(dbAudit.getType())){
                // 查询库存
                Kucun kucun = kucunMapper.selname(dbAudit.getName());
                if(kucun == null){
                    throw new RuntimeException("库存不存在");
                }
                // 判断库存
                if(kucun.getQuantity() < dbAudit.getQuantity()){
                    throw new RuntimeException("库存不足");
                }
                // 写入出库表
                Chuku chuku = new Chuku();
                chuku.setName(dbAudit.getName());
                chuku.setClient(dbAudit.getParther());
                chuku.setPrice(dbAudit.getPrice());
                chuku.setQuantity(dbAudit.getQuantity());
                chuku.setMoney(dbAudit.getMoney());
                chuku.setUser(dbAudit.getUser());
                chuku.setCktime(LocalDateTime.now());
                chukuMapper.addchuku(chuku);
                // 扣库存
                kucun.setQuantity(kucun.getQuantity() - dbAudit.getQuantity());

                kucunMapper.upkucun(kucun);
            }
        }
    }


    /**
     * 根据id查询审核表
     * @param id
     * @return
     */
    public Audit selid(Integer id) {
        return auditMapper.selid(id);
    }
}
