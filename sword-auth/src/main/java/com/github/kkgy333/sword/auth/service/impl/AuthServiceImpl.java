package com.github.kkgy333.sword.auth.service.impl;

import com.github.kkgy333.sword.auth.exception.UserInvalidException;
import com.github.kkgy333.sword.auth.model.User;
import com.github.kkgy333.sword.auth.service.AuthService;
import com.github.kkgy333.sword.auth.service.IUserService;
import com.github.kkgy333.sword.auth.system.entity.JwtAuthEntity;
import com.github.kkgy333.sword.auth.system.util.jwt.JWTInfo;
import com.github.kkgy333.sword.auth.system.util.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
@Service
public class AuthServiceImpl implements AuthService {

    private JwtTokenUtil jwtTokenUtil;
    private IUserService userService;

    @Autowired
    public AuthServiceImpl(
            JwtTokenUtil jwtTokenUtil,
            IUserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @Override
    public String login(JwtAuthEntity jwtAuthEntity) throws Exception {
        User info = userService.getByAccount(jwtAuthEntity.getUsername());
        if (!StringUtils.isEmpty(info.getId())) {
            return jwtTokenUtil.generateToken(new JWTInfo(info.getAccount(), info.getId() + "", info.getName()));
        }
        throw new UserInvalidException("用户不存在或账户密码错误!");
    }

    @Override
    public void validate(String token) throws Exception {
        jwtTokenUtil.getInfoFromToken(token);
    }

    @Override
    public String refresh(String oldToken) throws Exception {
        return jwtTokenUtil.generateToken(jwtTokenUtil.getInfoFromToken(oldToken));
    }
}
