package com.example.bs.service;

import com.example.bs.entity.Client;
import com.example.bs.entity.Suppiler;
import com.example.bs.mapper.SuCliMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuCliService {
    @Autowired
    private SuCliMapper suclMapper;

    /**
     * 查询供应商
     * @return
     */
    public List<Suppiler> selsu() {
        return suclMapper.selsu();
    }

    /**
     * 查询供应商名称
     * @return
     */
    public List<String> selsuname() {
        return suclMapper.selsuname();
    }

    /**
     * 根据name查询供应商
     * @return
     */
    public List<Suppiler> selsu2(String name) {
        return suclMapper.selsu2(name);
    }

    /**
     * 修改供应商
     * @param suppiler
     */
    public void upsu(Suppiler suppiler) {
        suclMapper.upsu(suppiler);
    }

    /**
     * 删除供应商
     * @param suppiler
     */
    public void delsu(Suppiler suppiler) {
        suclMapper.delsu(suppiler);
    }

    /**
     * 查询客户
     * @return
     */
    public List<Client> selcli() {
        return suclMapper.selcli();
    }

    /**
     * 查询客户名称
     * @return
     */
    public List<String> selcliname() {
        return suclMapper.selcliname();
    }

    /**
     * 根据name查询客户
     * @return
     */
    public List<Client> selcli2(String name) {
        return suclMapper.selcli2(name);
    }

    /**
     * 修改客户
     * @param client
     */
    public void upcli(Client client) {
        suclMapper.upcli(client);
    }

    /**
     * 删除客户
     * @param client
     */
    public void delcli(Client client) {
        suclMapper.delcli(client);
    }
}
