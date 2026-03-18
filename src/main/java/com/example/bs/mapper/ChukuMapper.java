package com.example.bs.mapper;

import com.example.bs.entity.Chuku;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChukuMapper {
    /**
     * 出库
     * @param chuku
     * @return
     */
    void addchuku(Chuku chuku);

    /**
     * 查询出库单
     * @return
     */
    List<Chuku> selchuku();

    /**
     * 查询今日出库
     * @return
     */
    int jinchuku();

    /**
     * 统计今日销售
     * @return
     */
    int jinsale();

    /**
     * 查询本月出库
     * @return
     */
    int yuechuku();

    /**
     * 统计本月销售
     * @return
     */
    int yuesale();

    /**
     * 统计本年销售
     * @return
     */
    int yearsale();

    /**
     * 分类查询
     * @param leibie
     * @return
     */
    List<Chuku> selchuku1(String leibie);

    /**
     * 获取所有类别
     * @return
     */
    List<String> selleibie1();
}
