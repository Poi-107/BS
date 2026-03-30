package com.example.bs.mapper;

import com.example.bs.entity.Kucun;
import com.example.bs.entity.Ruku;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface KucunMapper {
    /**
     * 查询库存
     * @return
     */
    List<Kucun> selkucun();


    /**
     * 添加库存1（没有该物品）
     * @param ruku
     */
    void addkucun(Ruku ruku);

    /**
     * 添加库存2（有该物品）
     * @param kucun
     */
    void upkucun(Kucun kucun);

    /**
     * 根据名称查询库存
     * @param name
     * @return
     */
    Kucun selname(String name);

    /**
     * 根据类别查询库存
     * @param leibie
     * @return
     */
    List<Kucun> selleibie(String leibie);

    /**
     * 获取所有类别
     * @return
     */
    List<String> selleibie2();

    /**
     * 根据code查询库存
     * @param code
     * @return
     */
    Kucun selcode(String code);

    /**
     * 修改库存
     * @param kucun
     */
    void upkucun2(Kucun kucun);
}
