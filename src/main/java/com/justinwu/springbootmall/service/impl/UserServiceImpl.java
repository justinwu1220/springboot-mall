package com.justinwu.springbootmall.service.impl;

import com.justinwu.springbootmall.dao.UserDao;
import com.justinwu.springbootmall.dto.UserAddressInfoRequest;
import com.justinwu.springbootmall.dto.UserLoginRequest;
import com.justinwu.springbootmall.dto.UserRegisterRequest;
import com.justinwu.springbootmall.model.UserAddressInfo;
import com.justinwu.springbootmall.model.User;
import com.justinwu.springbootmall.service.UserService;
import com.justinwu.springbootmall.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class UserServiceImpl implements UserService {

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        //檢查 註冊email是否已存在
        User user = userDao.getUserByEmail(userRegisterRequest.getEmail());

        if (user != null){
            log.warn("email {} 已被註冊", userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //使用 MD5 生成密碼的 hash value
        String hashedPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashedPassword);

        //創建帳號
        return userDao.createUser(userRegisterRequest);
    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public User login(UserLoginRequest userLoginRequest) {
        User user = userDao.getUserByEmail(userLoginRequest.getEmail());
        //檢查user是否存在
        if(user == null){
            log.warn("email {} 未被註冊", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "用戶不存在");
        }

        //使用 MD5 生成密碼的 hash value
        String hashedPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());

        //比對密碼
        if(user.getPassword().equals(hashedPassword)){
            String token = JwtUtil.createJwtToken(user);
            user.setToken(token);
            return user;
        }else{
            log.warn("email {} 的密碼錯誤", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "密碼錯誤");
        }
    }

    @Override
    public UserAddressInfo getUserAddressInfoById(Integer infoId) {
        return userDao.getUserAddressInfoById(infoId);
    }

    @Override
    public List<UserAddressInfo> getUserAddressInfoByUserId(Integer userId) {

        return userDao.getUserAddressInfoByUserId(userId);
    }

    @Override
    public Integer createUserAddressInfo(Integer userId, UserAddressInfoRequest userAddressInfoRequest) {
        //檢查 user 是否存在
        User user = userDao.getUserById(userId);

        if (user == null){
            log.warn("user {} 不存在", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "用戶不存在");
        }

        Integer userAddressInfoId = userDao.createUserAddressInfo(userId, userAddressInfoRequest);

        return userAddressInfoId;
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }
}
