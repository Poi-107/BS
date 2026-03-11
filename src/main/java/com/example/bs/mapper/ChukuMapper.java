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
}
