package com.github.kkgy333.sword.fabric.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.kkgy333.sword.fabric.server.mapper.UserMapper;
import com.github.kkgy333.sword.fabric.server.model.League;
import com.github.kkgy333.sword.fabric.server.model.User;
import com.github.kkgy333.sword.fabric.server.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public int init(User user) {
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            return 0;
        }
        List<User> users = listAll();
        for (User user1 : users) {
            if (StringUtils.equals(user.getUsername(), user1.getUsername())) {
                return update(user);
            }
        }
        return add(user);
    }

    @Override
    public int add(User user) {
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            return 0;
        }
        return userMapper.insert(user);
    }

    @Override
    public int update(User user) {
        return userMapper.updateById(user);
    }

    @Override
    public List<User> listAll() {
        return userMapper.selectList(null);
    }

    @Override
    public User get(String username) {

        Wrapper<User> queryWrapper = new QueryWrapper<User>();
        ((QueryWrapper<User>) queryWrapper).eq("username",username);
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }

}
