package com.example.demo.user.dto;

import lombok.Data;

@Data
public class UserDto {
    private String username;
    private String password;
    private String token;
    private String cid;
}
