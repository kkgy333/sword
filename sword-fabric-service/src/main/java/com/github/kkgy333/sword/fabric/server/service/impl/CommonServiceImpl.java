package com.github.kkgy333.sword.fabric.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.kkgy333.sword.fabric.server.mapper.UserMapper;
import com.github.kkgy333.sword.fabric.server.model.User;
import com.github.kkgy333.sword.fabric.server.service.CommonService;
import com.github.kkgy333.sword.fabric.server.utils.CacheUtil;
import com.github.kkgy333.sword.fabric.server.utils.MD5Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;

/**
 * Author: kkgy333
 * Date: 2018/7/23
 **/
@Service("commonService")
public class CommonServiceImpl implements CommonService {

    @Resource
    private UserMapper userMapper;

    @Override
    public String login(User user) {
        try {

            Wrapper<User> queryWrapper = new QueryWrapper<User>();
            ((QueryWrapper<User>) queryWrapper).eq("username",user.getUsername());
            User remoteUser = userMapper.selectOne(queryWrapper);
            if(remoteUser !=null) {

                if (MD5Util.verify(user.getPassword(), remoteUser.getPassword())) {
                    String token = UUID.randomUUID().toString();
                    CacheUtil.putString(user.getUsername(), token);
                    return token;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
