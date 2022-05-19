package com.example.demo.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Deal {
    private String id;
    private String device_id;
    private String member_id;
    private int amount;
    private String date;
    private String cid;
}
