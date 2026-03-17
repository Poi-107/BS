package com.example.bs.mapper;

import com.example.bs.entity.Client;
import com.example.bs.entity.Suppiler;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SuCliMapper {

    /**
     * 查询供应商
     * @return
     */
    List<Suppiler> selsu();

    /**
     * 查询供应商名称
     * @return
     */
    List<String> selsuname();

    /**
     * 根据name查询供应商
     * @return
     */
    List<Suppiler> selsu2(String name);

    /**
     * 修改供应商信息
     * @param suppiler
     */
    void upsu(Suppiler suppiler);

    /**
     * 删除供应商信息
     * @param suppiler
     */
    void delsu(Suppiler suppiler);

    /**
     * 查询客户
     * @return
     */
    List<Client> selcli();

    /**
     * 获取客户名称
     * @return
     */
    List<String> selcliname();

    /**
     * 根据name查询客户信息
     * @return
     */
    List<Client> selcli2(String name);

    /**
     * 修改客户信息
     * @param client
     */
    void upcli(Client client);

    /**
     * 删除客户信息
     * @param client
     */
    void delcli(Client client);
}
