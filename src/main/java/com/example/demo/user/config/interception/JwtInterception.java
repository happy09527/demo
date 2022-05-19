package com.example.demo.user.config.interception;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.user.entity.User;
import com.example.demo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import com.example.demo.user.mapper.UserMapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


public class JwtInterception implements HandlerInterceptor {
    @Autowired
    private UserMapper userMapper;

     @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");   //获取前端发来的请求携带的token
        if(!(handler instanceof  HandlerMethod)){
            return true;
        }
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("无token，请重新登录");
        }
        String username;
        try {
            username = JWT.decode(token).getAudience().get(0);        //解码token，获取账号
        } catch (JWTDecodeException j) {
            throw new RuntimeException("401");
        }
        //根据token中的userid查询数据库
        List<User> user = userMapper.find(username);
        if (user.isEmpty()) {
            throw new RuntimeException("用户不存在，请重新登录");
        }
        //用户密码加签验证token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.get(0).getPassword())).build();
        try {
            jwtVerifier.verify(token);  //验证token
        } catch (JWTVerificationException e) {
            throw new RuntimeException("401");
        }
        return true;
    }
}
