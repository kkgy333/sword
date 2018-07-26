package com.github.kkgy333.sword.fabric.server.interceptor;

import com.github.kkgy333.sword.fabric.server.utils.CacheUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Author: kkgy333
 * Date: 2018/7/26
 **/
public class LoginInterceptor implements HandlerInterceptor {

    /** 在请求被处理之前调用 */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 检查每个到来的请求对应的session域中是否有登录标识
        String token = (String) request.getSession().getAttribute("token");
        String username = (String) request.getSession().getAttribute("username");
        if (!StringUtils.equalsIgnoreCase(token, CacheUtil.getString(username))){
            response.sendRedirect("/login");
            return false;
        }
        return true;

    }
}