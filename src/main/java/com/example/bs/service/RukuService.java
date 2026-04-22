package com.example.bs.service;

import com.alibaba.fastjson.JSON;
import com.example.bs.entity.Audit;
import com.example.bs.entity.Kucun;
import com.example.bs.mapper.AuditMapper;
import com.example.bs.mapper.KucunMapper;
import com.example.bs.mapper.RukuMapper;
import com.example.bs.entity.Ruku;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RukuService {
    private static final String CACHE_KEY_RUKU = "cache:ruku:all";
    private static final Logger log = LoggerFactory.getLogger(RukuService.class);

    @Autowired
    private RukuMapper rukuMapper;

    @Autowired
    private KucunMapper kucunMapper;

    @Autowired
    private AuditMapper auditMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     *查询入库单
     * @return
     */
    public List<Ruku> selruku() {
        try {
            String cached = stringRedisTemplate.opsForValue().get(CACHE_KEY_RUKU);
            if (cached != null && !cached.isEmpty()) {
                return JSON.parseArray(cached, Ruku.class);
            }
        } catch (Exception e) {
            log.warn("Redis读取入库缓存失败，回退数据库: {}", e.getMessage());
        }

        List<Ruku> list = rukuMapper.selruku();
        try {
            stringRedisTemplate.opsForValue().set(CACHE_KEY_RUKU, JSON.toJSONString(list), 5, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.warn("Redis写入入库缓存失败: {}", e.getMessage());
        }
        return list;
    }

    /**
     * 添加入库单
     * @param ruku
     */
    @Transactional
    public void addruku(Ruku ruku) {

        Audit audit = new Audit();

        audit.setType("IN");
        audit.setCode(ruku.getCode());
        audit.setLeibie(ruku.getLeibie());
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
     * 批量添加入库单
     * @param rukuList
     */
    @Transactional
    public void batchAddRuku(List<Ruku> rukuList) {
        for (Ruku ruku : rukuList) {
            addruku(ruku);
        }
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

    /**
     * 按物品名称查询入库单
     * @param name
     * @return
     */
    public List<Ruku> selruku3(String name) {
        return rukuMapper.selruku3(name);
    }

    /**
     * 按供应商名称查询
     * @param supplier
     * @return
     */
    public List<Ruku> selruku4(String supplier) {
        return rukuMapper.selruku4(supplier);
    }

    /**
     * 按操作人查询
     * @param user
     * @return
     */
    public List<Ruku> selruku5(String user) {
        return rukuMapper.selruku5(user);
    }
}
