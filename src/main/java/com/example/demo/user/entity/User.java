package com.example.demo.user.entity;            //数据库表的实体类
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data      //自动添加getter和setter
@NoArgsConstructor   //创建无参构造
@AllArgsConstructor  //创建有参构造
public class User {
    private String username;
    private String password;
    private String power;
    private String phone;
    private String email;
    private String cid;
}
