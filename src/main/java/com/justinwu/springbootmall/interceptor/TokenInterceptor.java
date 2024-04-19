package com.justinwu.springbootmall.interceptor;

import com.justinwu.springbootmall.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    //在請求處理方法被調用前調用
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //從Header中拿到token
        String token = request.getHeader("token");

        if( !JwtUtil.checkJwtToken(token)){ //token驗證失敗
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "token驗證失敗");
        }
        return  true;
    }
}
