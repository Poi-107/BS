package com.example.bs.service;

import com.example.bs.entity.Kucun;
import com.example.bs.mapper.KucunMapper;
import com.example.bs.mapper.RukuMapper;
import com.example.bs.entity.Ruku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RukuService {
    @Autowired
    private RukuMapper rukuMapper;

    @Autowired
    private KucunMapper kucunMapper;

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
        rukuMapper.addruku(ruku);
        Kucun kucun = kucunMapper.selname(ruku.getName());
        if(kucun==null){
            kucunMapper.addkucun(ruku);
        }
        else {
            kucun.setQuantity(kucun.getQuantity()+ruku.getQuantity());
            kucunMapper.upkucun(kucun);
        }



    }
}