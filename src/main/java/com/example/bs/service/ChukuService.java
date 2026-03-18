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

    /**
     * 查询今日出库
     * @return
     */
    public int jinchuku() {
        return chukuMapper.jinchuku();
    }

    /**
     * 查询今日销售
     * @return
     */
    public int jinsale() {
        return chukuMapper.jinsale();
    }

    /**
     * 查询本月出库
     * @return
     */
    public int yuechuku() {
        return chukuMapper.yuechuku();
    }

    /**
     * 查询本月销售
     * @return
     */
    public int yuesale() {
        return chukuMapper.yuesale();
    }

    /**
     * 查询本年销售
     * @return
     */
    public int yearsale() {
        return chukuMapper.yearsale();
    }

    /**
     * 分类查询
     * @param leibie
     * @return
     */
    public List<Chuku> selchuku1(String leibie) {
        return chukuMapper.selchuku1(leibie);
    }

    /**
     * 获取所有类别
     * @return
     */
    public List<String> selleibie1() {
        return chukuMapper.selleibie1();
    }

    /**
     * 查询当前用户的出库单
     * @param user
     * @return
     */
    public List<Chuku> selchuku2(String user) {
        return chukuMapper.selchuku2(user);
    }

    /**
     * 按物品名称查询
     * @return
     */
    public List<Chuku> selchuku3(String name) {
        return chukuMapper.selchuku3(name);
    }

    /**
     * 按客户名称查询
     * @return
     */
    public List<Chuku> selchuku4(String client) {
        return chukuMapper.selchuku4(client);
    }

    /**
     * 按照操作人查询
     * @param user
     * @return
     */
    public List<Chuku> selchuku5(String user) {
        return chukuMapper.selchuku5(user);
    }
}
