package com.example.bs.service;

import com.example.bs.entity.Xinxi;
import com.example.bs.mapper.XinxiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class XinxiService {
    @Autowired
    private XinxiMapper xinxiMapper;

    /**
     * 查询通知信息
     * @return
     */
    public List<Xinxi> selxinxi(){
        return xinxiMapper.selxinxi();
    }

    /**
     * 添加通知信息
     * @param xinxi
     */
    public void addxinxi(Xinxi xinxi) {
        xinxiMapper.addxinxi(xinxi);
    }

    /**
     * 删除通知信息
     * @param id
     */
    public void delxinxi(Integer id) {
        xinxiMapper.delxinxi(id);
    }

    /**
     * 修改通知信息
     * @param xinxi
     */
    public void upxinxi(Xinxi xinxi) {
        xinxiMapper.upxinxi(xinxi);
    }
}
