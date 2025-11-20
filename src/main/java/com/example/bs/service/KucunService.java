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
}
