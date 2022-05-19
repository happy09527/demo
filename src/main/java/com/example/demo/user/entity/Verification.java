package com.example.demo.user.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Verification {
    private String code;
    private String rgb;
    private Date expireTime;
}
