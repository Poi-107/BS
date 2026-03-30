package com.example.bs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Xinxi {
    private int id;
    private String title;
    private String text;
    private int priority;
    private LocalDateTime crtime;
}
