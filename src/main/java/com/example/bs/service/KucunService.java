package com.example.bs.service;

import com.alibaba.fastjson.JSON;
import com.example.bs.entity.Kucun;
import com.example.bs.entity.Ruku;
import com.example.bs.tools.yujing.InventoryWarningService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.example.bs.mapper.KucunMapper;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class KucunService {
    private static final String CACHE_KEY_KUCUN = "cache:kucun:all";
    private static final Logger log = LoggerFactory.getLogger(KucunService.class);

    @Autowired
    private KucunMapper kucunMapper;
    @Autowired
    private InventoryWarningService inventoryWarningService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 查询库存
     * @return
     */
    public List<Kucun> selkucun() {
        try {
            String cached = stringRedisTemplate.opsForValue().get(CACHE_KEY_KUCUN);
            if (cached != null && !cached.isEmpty()) {
                List<Kucun> list = JSON.parseArray(cached, Kucun.class);
                return inventoryWarningService.enrichWithWarning(list);
            }
        } catch (Exception e) {
            log.warn("Redis读取库存缓存失败，回退数据库: {}", e.getMessage());
        }

        List<Kucun> list = kucunMapper.selkucun();
        try {
            stringRedisTemplate.opsForValue().set(CACHE_KEY_KUCUN, JSON.toJSONString(list), 5, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.warn("Redis写入库存缓存失败: {}", e.getMessage());
        }
        return inventoryWarningService.enrichWithWarning(list);
    }

    /**
     * 添加入库
     * @param ruku
     */
    public void addkucun(Ruku ruku) {
        kucunMapper.addkucun(ruku);
        try {
            stringRedisTemplate.delete(CACHE_KEY_KUCUN);
        } catch (Exception e) {
            log.warn("Redis删除库存缓存失败: {}", e.getMessage());
        }
    }

    /**
     * 根据name查询库存
     * @param name
     * @return
     */
    public Kucun selname(String name) {
        return kucunMapper.selname(name);
    }

    /**
     * 根据类别查询库存
     * @param leibie
     * @return
     */
    public List<Kucun> selleibie(String leibie) {
        return inventoryWarningService.enrichWithWarning(kucunMapper.selleibie(leibie));
    }

    /**
     * 获取所有类别
     * @return
     */
    public List<String> selleibie2() {
        return kucunMapper.selleibie2();
    }

    /**
     * 根据code查询库存
     * @param code
     * @return
     */
    public Kucun selcode(String code) {
        return kucunMapper.selcode(code);
    }


    /**
     * 修改库存
     * @param kucun
     */
    public void upkucun2(Kucun kucun) {
        kucunMapper.upkucun2(kucun);
        try {
            stringRedisTemplate.delete(CACHE_KEY_KUCUN);
        } catch (Exception e) {
            log.warn("Redis删除库存缓存失败: {}", e.getMessage());
        }
    }
}
