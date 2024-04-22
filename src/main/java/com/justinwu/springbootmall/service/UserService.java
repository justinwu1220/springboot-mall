package com.justinwu.springbootmall.service;

import com.justinwu.springbootmall.dto.UserAddressInfoRequest;
import com.justinwu.springbootmall.dto.UserLoginRequest;
import com.justinwu.springbootmall.dto.UserRegisterRequest;
import com.justinwu.springbootmall.model.UserAddressInfo;
import com.justinwu.springbootmall.model.User;

import java.util.List;

public interface UserService {

    Integer register(UserRegisterRequest userRegisterRequest);
    User getUserById(Integer userId);
    User login(UserLoginRequest userLoginRequest);

    UserAddressInfo getUserAddressInfoById(Integer checkoutInfoId);
    List<UserAddressInfo> getUserAddressInfoByUserId(Integer userId);
    Integer createUserAddressInfo(UserAddressInfoRequest checkoutInfoRequest);
}
