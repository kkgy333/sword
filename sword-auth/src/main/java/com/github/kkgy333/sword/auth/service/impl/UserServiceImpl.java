package com.github.kkgy333.sword.auth.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.kkgy333.sword.auth.mapper.UserMapper;
import com.github.kkgy333.sword.auth.model.User;
import com.github.kkgy333.sword.auth.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: kkgy333
 * Date: 2018/7/5
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

//    @Override
//    public int setStatus(Integer userId, int status) {
//        return this.baseMapper.setStatus(userId, status);
//    }
//
//    @Override
//    public int changePwd(Integer userId, String pwd) {
//        return this.baseMapper.changePwd(userId, pwd);
//    }
//
//    @Override
//    public List<Map<String, Object>> selectUsers(DataScope dataScope, String name, String beginTime, String endTime, Integer deptid) {
//        return this.baseMapper.selectUsers(dataScope, name, beginTime, endTime, deptid);
//    }
//
//    @Override
//    public int setRoles(Integer userId, String roleIds) {
//        return this.baseMapper.setRoles(userId, roleIds);
//    }

    @Override
    public User getByAccount(String account) {
        return this.baseMapper.getByAccount(account);
    }


    @Override
    public List<User> selectListBySQL() {
        return baseMapper.selectListBySQL();
    }
}
