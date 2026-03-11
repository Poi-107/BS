package com.example.bs.service;

import com.example.bs.entity.Audit;
import com.example.bs.entity.Chuku;
import com.example.bs.entity.Kucun;
import com.example.bs.mapper.AuditMapper;
import com.example.bs.mapper.ChukuMapper;
import com.example.bs.mapper.KucunMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChukuService {
    @Autowired
    private ChukuMapper chukuMapper;
    @Autowired
    private KucunService kucunService;
    @Autowired
    private KucunMapper kucunMapper;
    @Autowired
    private AuditMapper auditMapper;


    /**
     * 出库
     * @param chuku
     * @return
     */
    @Transactional
    public void addchuku(Chuku chuku) {
//        发送审核
        Audit audit = new Audit();

        audit.setType("OUT");
        audit.setName(chuku.getName());
        audit.setParther(chuku.getClient());
        audit.setPrice(chuku.getPrice());
        audit.setQuantity(chuku.getQuantity());
        audit.setMoney(chuku.getMoney());
        audit.setUser(chuku.getUser());
        audit.setCreatetime(LocalDateTime.now());
        audit.setStatus(0); // 待审核

        auditMapper.addaudit(audit);

//        //先查询是否存在
//        Kucun kucun =kucunService.selname(chuku.getName());
//        //再判断数量是否充足
//        if(kucun!=null){
//            if(kucun.getQuantity()>=chuku.getQuantity()){
//                //修改库存数量
//                kucun.setQuantity(kucun.getQuantity()-chuku.getQuantity());
//                kucunMapper.upkucun(kucun);
//                return chukuMapper.chuku(chuku);
//            }
//            return 0;
//        }else{
//            return 0;
//
//        }

    }

    /**
     * 查询出库单
     * @return
     */
    public List<Chuku> selchuku() {
        return chukuMapper.selchuku();
    }
}
