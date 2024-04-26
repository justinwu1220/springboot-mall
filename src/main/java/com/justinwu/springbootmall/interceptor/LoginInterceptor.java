package com.justinwu.springbootmall.interceptor;

import com.justinwu.springbootmall.constant.Authority;
import com.justinwu.springbootmall.model.User;
import com.justinwu.springbootmall.service.UserService;
import com.justinwu.springbootmall.tool.AdminAuthorityCheck;
import com.justinwu.springbootmall.tool.PassToken;
import com.justinwu.springbootmall.tool.UserLoginToken;
import com.justinwu.springbootmall.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    //在請求處理方法被調用前調用
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //從Header中拿到token
        String token = request.getHeader("token");

        //如果不是映射到方法，直接通過
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        //檢查方法是否有PassToken註解，有的話跳過驗證，直接通過
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }

        //檢查方法是否有UserLoginToken註解，有的話必須經過驗證
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                //驗證
                if (!JwtUtil.checkJwtToken(token)) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "無token，token過期或不正確，請重新登入");
                }

                // 獲取 token 中的 userId
                Claims claims = JwtUtil.parseJwtToken(token);
                Integer userId = Integer.valueOf(claims.getId());

                //查詢用戶是否存在
                User user = userService.getUserById(userId);
                if (user == null) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "用戶不存在，請重新登入");
                }

                return true;
            }
        }

        //檢查方法是否有AdminAuthorityCheck註解，有的話必須經過驗證
        if (method.isAnnotationPresent(AdminAuthorityCheck.class)) {
            AdminAuthorityCheck adminAuthorityCheck = method.getAnnotation(AdminAuthorityCheck.class);
            if (adminAuthorityCheck.required()) {
                //驗證
                if (!JwtUtil.checkJwtToken(token)) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "無token，token過期或不正確，請重新登入");
                }

                // 獲取 token 中的 userId
                Claims claims = JwtUtil.parseJwtToken(token);
                Integer userId = Integer.valueOf(claims.getId());

                //查詢用戶是否存在
                User user = userService.getUserById(userId);
                if (user == null) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "用戶不存在，請重新登入");
                }

                if(user.getAuthority().equals(Authority.ADMIN)){
                    return true;
                } else{
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "用戶權限不足");
                }
            }
        }

        //throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "訪問路徑錯誤或無註解權限，不允許通過");
        return false;
    }
}
