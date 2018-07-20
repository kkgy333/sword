package com.github.kkgy333.sword.auth.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.kkgy333.sword.auth.model.User;
import com.github.kkgy333.sword.auth.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("jwt")
public class AuthController {

//    @Value("${jwt.token-header}")
//    private String tokenHeader;
//
//    @Autowired
//    private AuthService authService;
//
//    @RequestMapping(value = "token", method = RequestMethod.POST)
//    public RestResponse<String> createAuthenticationToken(
//            @RequestBody JwtAuthEntity jwtAuthEntity) throws Exception {
//        log.info(jwtAuthEntity.getUsername()+" require logging...");
//        final String token = authService.login(jwtAuthEntity);
//        return new RestResponse<>().data(token);
//    }
//
//    @RequestMapping(value = "refresh", method = RequestMethod.GET)
//    public RestResponse<String> refreshAndGetAuthenticationToken(
//            HttpServletRequest request) throws Exception {
//        String token = request.getHeader(tokenHeader);
//        String refreshedToken = authService.refresh(token);
//        return new RestResponse<>().data(refreshedToken);
//    }
//
//    @RequestMapping(value = "verify", method = RequestMethod.GET)
//    public RestResponse<?> verify(String token) throws Exception {
//        authService.validate(token);
//        return new RestResponse<>();
//    }

    @Autowired
    private IUserService userService;

    /**
     * 分页 PAGE
     */
    @GetMapping("/test")
    public IPage<User> test() {
        return userService.selectPage(new Page<User>(0, 12), null);
    }
}
