package com.example.demo.user.entity;

import lombok.Data;

@Data
public class Files {
    private Integer id;
    private String name;
    private String type;
    private long size;
    private String url;
    private String md5;
    private String cid;
}
