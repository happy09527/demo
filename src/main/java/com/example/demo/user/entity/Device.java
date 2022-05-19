package com.example.demo.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    private String id;
    private String kind;
    private String name;
    private String network_id;
    private String state;
    private String cid;
}
