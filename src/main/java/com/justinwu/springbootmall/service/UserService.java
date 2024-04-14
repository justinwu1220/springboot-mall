package com.justinwu.springbootmall.service;

import com.justinwu.springbootmall.dto.UserRegisterRequest;
import com.justinwu.springbootmall.model.User;

public interface UserService {

    Integer register(UserRegisterRequest userRegisterRequest);

    User getUserById(Integer userId);
}
