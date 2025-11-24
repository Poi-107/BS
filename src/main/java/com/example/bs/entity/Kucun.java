package com.example.bs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 库存
 */
public class Kucun {
    private Integer id;
    private String name;
    private String supplier;
    private Integer price;
    private Integer quantity;
    private Integer money;
}
