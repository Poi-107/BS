package com.example.bs.service;

import com.example.bs.mapper.RukuMapper;
import com.example.bs.pojo.Ruku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RukuService {
    @Autowired
    private RukuMapper rukuMapper;

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
    public void addruku(Ruku ruku) {
        rukuMapper.addruku(ruku);
    }
}
