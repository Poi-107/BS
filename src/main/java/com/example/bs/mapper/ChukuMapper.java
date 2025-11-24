package com.example.bs.mapper;

import com.example.bs.entity.Chuku;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChukuMapper {
    /**
     * 出库
     * @param chuku
     * @return
     */
    int chuku(Chuku chuku);
}
