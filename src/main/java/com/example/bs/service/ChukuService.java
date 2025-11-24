package com.example.bs.service;

import com.example.bs.entity.Chuku;
import com.example.bs.entity.Kucun;
import com.example.bs.mapper.ChukuMapper;
import com.example.bs.mapper.KucunMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChukuService {
    @Autowired
    private ChukuMapper chukuMapper;
    @Autowired
    private KucunService kucunService;
    @Autowired
    private KucunMapper kucunMapper;


    /**
     * 出库
     * @param chuku
     * @return
     */
    @Transactional
    public int chuku(Chuku chuku) {
        //先查询是否存在
        Kucun kucun =kucunService.selname(chuku.getName());
        //再判断数量是否充足
        if(kucun!=null){
            if(kucun.getQuantity()>=chuku.getQuantity()){
                //修改库存数量
                kucun.setQuantity(kucun.getQuantity()-chuku.getQuantity());
                kucun.setMoney(kucun.getMoney()-chuku.getMoney());
                kucunMapper.upkucun(kucun);
                return chukuMapper.chuku(chuku);
            }
            return 0;
        }else{
            return 0;

        }

    }
}
