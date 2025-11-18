package com.example.bs.mapper;

import com.example.bs.pojo.Ruku;
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
}
