package com.justinwu.springbootmall.controller;

import com.justinwu.springbootmall.dto.UserAddressInfoRequest;
import com.justinwu.springbootmall.dto.UserLoginRequest;
import com.justinwu.springbootmall.dto.UserRegisterRequest;
import com.justinwu.springbootmall.model.UserAddressInfo;
import com.justinwu.springbootmall.model.User;
import com.justinwu.springbootmall.service.UserService;
import com.justinwu.springbootmall.tool.PassToken;
import com.justinwu.springbootmall.tool.UserLoginToken;
import com.justinwu.springbootmall.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PassToken//不需登入權限
    @PostMapping("/users/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest){
        Integer userId = userService.register(userRegisterRequest);

        User user = userService.getUserById(userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PassToken//不需登入權限
    @PostMapping("/users/login")
    public ResponseEntity<User> login(@RequestBody @Valid UserLoginRequest userLoginRequest){
        User user = userService.login(userLoginRequest);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @UserLoginToken //需要登入權限
    @GetMapping("/users/addressInfo")
    public ResponseEntity<List<UserAddressInfo>> getUserAddressInfo(@RequestHeader("token") String token){
        // 獲取 token 中的 userId
        Claims claims = JwtUtil.parseJwtToken(token);
        Integer userId = Integer.valueOf(claims.getId());

        List<UserAddressInfo> infoList = userService.getUserAddressInfoByUserId(userId);

        return ResponseEntity.status(HttpStatus.OK).body(infoList);
    }


    @UserLoginToken //需要登入權限
    @PostMapping("/users/addressInfo")
    public ResponseEntity<UserAddressInfo> createUserAddressInfo(@RequestHeader("token") String token,
                                                                 @RequestBody @Valid UserAddressInfoRequest userAddressInfoRequest){

        // 獲取 token 中的 userId
        Claims claims = JwtUtil.parseJwtToken(token);
        Integer userId = Integer.valueOf(claims.getId());

        //建立info後返回自動增加的infoId
        Integer infoId = userService.createUserAddressInfo(userId, userAddressInfoRequest);
        //透過infoId查詢建立好的info並返回給前端
        UserAddressInfo info = userService.getUserAddressInfoById(infoId);

        return ResponseEntity.status(HttpStatus.OK).body(info);
    }
}
