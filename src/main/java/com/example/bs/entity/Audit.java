package com.example.bs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 审核
 */
public class Audit {
    private Integer id;
    private String type;
    private String name;
    private String parther;
    private Integer price;
    private Integer quantity;
    private Integer money;
    private String user;
    private LocalDateTime createtime;
    private Integer status;
    private String reviewer;
    private LocalDateTime reviewertime;
    private String  remark;

}
