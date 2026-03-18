package com.example.bs.service;

import com.example.bs.entity.Kucun;
import com.example.bs.entity.Ruku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.bs.mapper.KucunMapper;

import java.util.List;

@Service
public class KucunService {
    @Autowired
    private KucunMapper kucunMapper;

    /**
     * 查询库存
     * @return
     */
    public List<Kucun> selkucun() {
        return kucunMapper.selkucun();
    }

    /**
     * 添加入库
     * @param ruku
     */
    public void addkucun(Ruku ruku) {
        kucunMapper.addkucun(ruku);
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
        return kucunMapper.selleibie(leibie);
    }

    /**
     * 获取所有类别
     * @return
     */
    public List<String> selleibie2() {
        return kucunMapper.selleibie2();
    }
}
