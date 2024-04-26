package com.justinwu.springbootmall.dao;

import com.justinwu.springbootmall.dto.UserAddressInfoRequest;
import com.justinwu.springbootmall.dto.UserRegisterRequest;
import com.justinwu.springbootmall.model.UserAddressInfo;
import com.justinwu.springbootmall.model.User;

import java.util.List;

public interface UserDao {
    Integer createUser(UserRegisterRequest userRegisterRequest);
    User getUserById(Integer userId);
    User getUserByEmail(String email);
    UserAddressInfo getUserAddressInfoById(Integer userAddressInfoId);
    List<UserAddressInfo> getUserAddressInfoByUserId(Integer userId);
    Integer createUserAddressInfo(Integer userId, UserAddressInfoRequest userAddressInfoRequest);
    List<User> getAllUsers();
}
