package com.github.kkgy333.sword.fabric.server.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.kkgy333.sword.fabric.server.model.User;
import com.github.kkgy333.sword.fabric.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@RestController
@RequestMapping("test")
public class TestController {
    @Autowired
    private UserService userService;

    @GetMapping("/test")
    public List<User> test() {
        return userService.listAll();
    }

}
