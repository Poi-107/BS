package com.example.bs.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 用户类
 */
public class User {
    private Integer id;
    private String username;
    private String password;
    private Integer per;
    private String avatar;
}
