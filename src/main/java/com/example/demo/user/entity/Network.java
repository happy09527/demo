package com.example.demo.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data      //自动添加getter和setter
@NoArgsConstructor   //创建无参构造
@AllArgsConstructor
public class Network {
    private String id;
    private String name;
    private String address;
    private String company;
    private String phone;
    private String state;
    private double lng = -1;
    private double lat = -1;
    private String cid;
}
