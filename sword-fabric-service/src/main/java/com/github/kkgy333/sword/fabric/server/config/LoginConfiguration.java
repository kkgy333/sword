package com.github.kkgy333.sword.fabric.server.config;

import com.github.kkgy333.sword.fabric.server.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Author: kkgy333
 * Date: 2018/7/26
 **/

@Configuration
public class LoginConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器
        LoginInterceptor loginInterceptor = new LoginInterceptor();
        InterceptorRegistration loginRegistry = registry.addInterceptor(loginInterceptor);
        // 拦截路径
        loginRegistry.addPathPatterns("/");
        loginRegistry.addPathPatterns("");
        loginRegistry.addPathPatterns("/league/*");
        loginRegistry.addPathPatterns("/org/*");
        loginRegistry.addPathPatterns("/orderer/*");
        loginRegistry.addPathPatterns("/peer/*");
        loginRegistry.addPathPatterns("/channel/*");
        loginRegistry.addPathPatterns("/chaincode/*");
        loginRegistry.addPathPatterns("/state/*");
        //loginRegistry.addPathPatterns("/trace/*");
        loginRegistry.addPathPatterns("/app/*");
        loginRegistry.addPathPatterns("/index");

    }

}
