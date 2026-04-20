package com.example.bs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
//DTO 临时对象
public class ChukuDailyDemand {
    private String name;
    private LocalDate day;
    private Integer quantity;
}

