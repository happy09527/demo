package com.example.demo.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    private String id;
    private String name;
    private int money;
    private String phone;
    private String created_time;
    private String cid;
}
