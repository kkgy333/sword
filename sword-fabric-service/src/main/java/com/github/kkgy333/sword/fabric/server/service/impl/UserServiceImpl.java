package com.github.kkgy333.sword.fabric.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.kkgy333.sword.fabric.server.dao.mapper.*;
import com.github.kkgy333.sword.fabric.server.dao.*;
import com.github.kkgy333.sword.fabric.server.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Override
    public boolean init(User user) {
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            return false;
        }
        List<User> users = super.list(null);
        for (User user1 : users) {
            if (StringUtils.equals(user.getUsername(), user1.getUsername())) {
                return super.updateById(user);
            }
        }
        return super.save(user);
    }

    @Override
    public boolean add(User user) {
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            return false;
        }
        return super.save(user);
    }

    @Override
    public boolean update(User user) {
        return super.updateById(user);
    }

    @Override
    public List<User> listAll() {
        return super.list(null);
    }

    @Override
    public User get(String username) {

        Wrapper<User> queryWrapper = new QueryWrapper<User>();
        ((QueryWrapper<User>) queryWrapper).eq("username",username);
        User user = super.getOne(queryWrapper);
        return user;
    }

}
