package com.github.kkgy333.sword.fabric.server.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.github.kkgy333.sword.fabric.server.dao.User;

import java.util.List;

/**
 * 作者：Aberic on 2018/6/27 22:11
 * 邮箱：abericyang@gmail.com
 */
public interface UserService extends IService<User> {

    boolean init(User user);

    boolean add(User user);

    boolean update(User user);

    List<User> listAll();

    User get(String username);
}
