package com.example.bs.mapper;

import com.example.bs.entity.Ruku;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RukuMapper {
    /**
     * 查询入库单
     * @return
     */
    List<Ruku> selruku();

    /**
     * 添加入库单
     * @param ruku
     */
    void addruku(Ruku ruku);

    /**
     * 查询今日入库
     * @return
     */
    int jinruku();

    /**
     * 今日采购
     * @return
     */
    int jinpur();

    /**
     * 查询本月入库
     * @return
     */
    int yueruku();

    /**
     * 本月采购
     * @return
     */
    int yuepur();

    /**
     * 查询本年采购
     * @return
     */
    int yearpur();
    /**
     * 分类查询
     * @param ruku
     * @return
     */
    List<Ruku> selruku1(String leibie);

    /**
     * 获取所有类别
     * @return
     */
    List<String> selleibie();

    /**
     * 根据username查询入库单
     * @param user
     * @return
     */
    List<Ruku> selruku2(String user);
}
