package com.justinwu.springbootmall.dao;

import com.justinwu.springbootmall.dto.UserRegisterRequest;
import com.justinwu.springbootmall.model.User;

public interface UserDao {
    Integer createUser(UserRegisterRequest userRegisterRequest);
    User getUserById(Integer userId);
}
