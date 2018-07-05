package com.github.kkgy333.sword.auth.service;

import com.github.kkgy333.sword.auth.system.entity.JwtAuthEntity;

public interface AuthService {
    String login(JwtAuthEntity jwtAuthEntity) throws Exception;
    String refresh(String oldToken) throws Exception;
    void validate(String token) throws Exception;
}