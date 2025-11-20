package com.example.bs.service;

import com.example.bs.mapper.LogMapper;
import com.example.bs.entity.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {

    @Autowired
     private LogMapper logMapper;

    /**
     * 查询所有日志
     */
    public List<Log> sellog() {
        return logMapper.sellog();
    }

    /**
     * 添加日志
     * @param logs
     */
    public void addlog(Log logs) {
         logMapper.addlog(logs);
    }
}
