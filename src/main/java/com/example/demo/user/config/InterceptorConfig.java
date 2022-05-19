package com.example.demo.user.config;

import com.example.demo.user.config.interception.JwtInterception;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterception())
                .addPathPatterns("/**")     // 拦截请求，通过判断token是否合法决定是否需要登录
                .excludePathPatterns("/user/login","/user/register","/file/**","/user/Ver","/user/invite","/user/findInvite","/user/cidByUsername");   //除了访问这些接口的请求都会被拦截
    }
    @Bean
    public JwtInterception jwtInterception(){
        return new JwtInterception();
    }
}
