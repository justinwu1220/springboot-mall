package com.justinwu.springbootmall.interceptor;

import com.justinwu.springbootmall.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    //在請求處理方法被調用前調用
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //從Header中拿到token
        String token = request.getHeader("token");

        if( !JwtUtil.checkJwtToken(token)){ //token驗證失敗
            System.out.println("token not find");
            return false;
        }
        System.out.println("token find");
        return  true;
    }
}
