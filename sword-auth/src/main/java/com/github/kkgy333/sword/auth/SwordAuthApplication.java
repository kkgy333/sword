package com.github.kkgy333.sword.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@ComponentScan(basePackages = {
        "com.github.kkgy333.sword.auth.config",
        "com.github.kkgy333.sword.auth.controller",
        "com.github.kkgy333.sword.auth.service"})
public class SwordAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(SwordAuthApplication.class, args);
    }
}
