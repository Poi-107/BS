package com.example.bs.mapper;

import com.example.bs.entity.Log;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LogMapper {

    /**
     * 保存日志
     * @param logs
     */
    void addlog(Log logs);

    /**
     * 查询日志
     * @return
     */
    List<Log> sellog();
}
