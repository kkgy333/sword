package com.github.kkgy333.sword.auth.controller;


import com.github.kkgy333.sword.auth.service.AuthService;
import com.github.kkgy333.sword.auth.system.entity.JwtAuthEntity;
import com.github.kkgy333.sword.auth.system.msg.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("jwt")
@Slf4j
public class AuthController {

    @Value("${jwt.token-header}")
    private String tokenHeader;

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "token", method = RequestMethod.POST)
    public RestResponse<String> createAuthenticationToken(
            @RequestBody JwtAuthEntity jwtAuthEntity) throws Exception {
        log.info(jwtAuthEntity.getUsername()+" require logging...");
        final String token = authService.login(jwtAuthEntity);
        return new RestResponse<>().data(token);
    }

    @RequestMapping(value = "refresh", method = RequestMethod.GET)
    public RestResponse<String> refreshAndGetAuthenticationToken(
            HttpServletRequest request) throws Exception {
        String token = request.getHeader(tokenHeader);
        String refreshedToken = authService.refresh(token);
        return new RestResponse<>().data(refreshedToken);
    }

    @RequestMapping(value = "verify", method = RequestMethod.GET)
    public RestResponse<?> verify(String token) throws Exception {
        authService.validate(token);
        return new RestResponse<>();
    }
}
