package com.example.bs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 入库单
 */
public class Ruku {
    private Integer id;
    private String leibie;
    private String name;
    private String supplier;
    private Integer price;
    private Integer quantity;
    private Integer money;
    private LocalDateTime rktime;
    private String user;
}
