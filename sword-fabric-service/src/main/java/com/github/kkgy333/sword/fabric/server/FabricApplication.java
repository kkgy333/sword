package com.github.kkgy333.sword.fabric.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@ComponentScan(basePackages = {
        "com.github.kkgy333.sword.fabric.server.config",
        "com.github.kkgy333.sword.fabric.server.controller",
        "com.github.kkgy333.sword.fabric.server.service",
        "com.github.kkgy333.sword.fabric.server.utils"})
public class FabricApplication {
    public static void main(String[] args) {
        SpringApplication.run(FabricApplication.class, args);
    }
}
