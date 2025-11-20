package com.example.bs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Log {
    private Integer id;
    private Integer empid;
    private String name;
    private LocalDateTime time;
    private String target;
    private String action;

}
