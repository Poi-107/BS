package com.example.bs.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ruku {
    private Integer id;
    private String name;
    private String supplier;
    private Integer price;
    private Integer quantity;
    private Integer money;
    private LocalDateTime rktime;
    private String user;
}
