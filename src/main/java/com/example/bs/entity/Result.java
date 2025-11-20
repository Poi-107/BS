package com.example.bs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//接口响应类
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private String code;
    private String message;
    private Object data;

    //    增删改响应
    public static Result success(){
        return new Result("1","success",null);
    }
    //    查询响应
    public static Result success(Object data){
        return new Result("1","success",data);
    }
    //    删除响应
    public static Result error(String message){
        return new Result("0",message,null);
    }
}
