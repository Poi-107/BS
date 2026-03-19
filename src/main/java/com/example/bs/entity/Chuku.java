package com.example.bs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 出库单
 */
public class Chuku {
    private Integer id;
    private String code;
    private String leibie;
    private String name;
    private String client;
    private Integer price;
    private Integer quantity;
    private Integer money;
    private LocalDateTime cktime;
    private String user;
}
