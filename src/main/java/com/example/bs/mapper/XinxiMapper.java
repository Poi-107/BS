package com.example.bs.mapper;

import com.example.bs.entity.Xinxi;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface XinxiMapper {

    /**
     * 查询通知信息
     * @return
     */
    List<Xinxi> selxinxi();

    /**
     * 添加通知信息
     * @param xinxi
     */
    void addxinxi(Xinxi xinxi);

    /**
     * 删除通知信息
     * @param id
     */
    void delxinxi(Integer id);

    /**
     * 修改通知信息
     * @param xinxi
     */
    void upxinxi(Xinxi xinxi);
}
