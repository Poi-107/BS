package com.example.bs.service;

import com.example.bs.entity.Audit;
import com.example.bs.entity.Kucun;
import com.example.bs.mapper.AuditMapper;
import com.example.bs.mapper.KucunMapper;
import com.example.bs.mapper.RukuMapper;
import com.example.bs.entity.Ruku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
public class RukuService {
    @Autowired
    private RukuMapper rukuMapper;

    @Autowired
    private KucunMapper kucunMapper;

    @Autowired
    private AuditMapper auditMapper;

    /**
     *查询入库单
     * @return
     */
    public List<Ruku> selruku() {
        return rukuMapper.selruku();
    }

    /**
     * 添加入库单
     * @param ruku
     */
    @Transactional
    public void addruku(Ruku ruku) {
//        rukuMapper.addruku(ruku);
        Audit audit = new Audit();

        audit.setType("IN");
        audit.setName(ruku.getName());
        audit.setParther(ruku.getSupplier());
        audit.setPrice(ruku.getPrice());
        audit.setQuantity(ruku.getQuantity());
        audit.setMoney(ruku.getMoney());
        audit.setUser(ruku.getUser());
        audit.setCreatetime(LocalDateTime.now());
        audit.setStatus(0); // 待审核

        auditMapper.addaudit(audit);
    }

    /**
     * 查询今日入库
     * @return
     */
    public int jinruku() {
        return rukuMapper.jinruku();
    }

    /**
     * 查询今日采购
     * @return
     */
    public int jinpur() {
        return rukuMapper.jinpur();
    }

    /**
     * 查询本月入库
     * @return
     */
    public int yueruku() {
        return rukuMapper.yueruku();
    }

    /**
     * 查询本月采购
     * @return
     */
    public int yuecpur() {
        return rukuMapper.yuepur();
    }

    /**
     * 查询本年采购
     * @return
     */
    public int yearpur() {
        return rukuMapper.yearpur();
    }


    /**
     * 分类查询入库单
     * @param leibie
     * @return
     */
    public List<Ruku> selruku1(String leibie) {
        return rukuMapper.selruku1(leibie);
    }

    /**
     * 获取所有类别
     * @return
     */
    public List<String> selleibie() {
        return rukuMapper.selleibie();
    }

    /**
     * 根据username查询入库单
     * @param user
     * @return
     */
    public List<Ruku> selruku2(String user) {
        return rukuMapper.selruku2(user);
    }
}