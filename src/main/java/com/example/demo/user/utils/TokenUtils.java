package com.example.demo.user.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.apache.commons.lang3.time.DateUtils;
import java.util.Date;

public class TokenUtils  {
    private static final long EXPIRE_TIME = 60 * 60 * 2000;
    public static String genToken(String username,String password){       //生成token
        Date date = new Date(System.currentTimeMillis()+EXPIRE_TIME);
        return JWT.create().withAudience(username) // 将 user id 保存到 token 里面,作为载荷
                .withExpiresAt(date) //两小时  后token过期
                .sign(Algorithm.HMAC256(password)); // 以 password 作为 token 的密钥
    }
}
