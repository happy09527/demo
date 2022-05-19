package com.example.demo.user.entity;

import lombok.Data;


@Data
public class Log {
    private int id;
    private String type;
    private String message;
    private String time;
    private String cid;
}
