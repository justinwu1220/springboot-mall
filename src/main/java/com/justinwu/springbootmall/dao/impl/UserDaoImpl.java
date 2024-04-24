package com.justinwu.springbootmall.dao.impl;

import com.justinwu.springbootmall.constant.Authority;
import com.justinwu.springbootmall.dao.UserDao;
import com.justinwu.springbootmall.dto.UserAddressInfoRequest;
import com.justinwu.springbootmall.dto.UserRegisterRequest;
import com.justinwu.springbootmall.model.UserAddressInfo;
import com.justinwu.springbootmall.model.User;
import com.justinwu.springbootmall.rowmapper.UserAddressInfoRowMapper;
import com.justinwu.springbootmall.rowmapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDaoImpl implements UserDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer createUser(UserRegisterRequest userRegisterRequest) {
        String sql = "INSERT INTO user(email, password, authority, created_date, last_modified_date)" +
                " VALUES (:email, :password, :authority, :createdDate, :lastModifiedDate)";

        //用map將變數值帶入sql中
        Map<String, Object> map = new HashMap<>();
        map.put("email", userRegisterRequest.getEmail());
        map.put("password", userRegisterRequest.getPassword());

        //預設帳戶權限為CLIENT
        map.put("authority", Authority.CLIENT.toString());

        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        //取得資料庫自動產生的productId
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
        int userId = keyHolder.getKey().intValue();

        return userId;
    }

    @Override
    public User getUserById(Integer userId) {
        String sql = "SELECT user_id, email, password, authority, created_date, last_modified_date" +
                " FROM user WHERE user_id = :userId";

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        //namedParameterJdbcTemplate.query會返回list
        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());
        //userList若為空，則返回null
        if(userList.size()>0) {
            return userList.get(0);
        }
        else{
            return null;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        String sql = "SELECT user_id, email, password, authority, created_date, last_modified_date" +
                " FROM user WHERE email = :email";

        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        //namedParameterJdbcTemplate.query會返回list
        List<User> userList = namedParameterJdbcTemplate.query(sql, map, new UserRowMapper());
        //userList若為空，則返回null
        if(userList.size()>0) {
            return userList.get(0);
        }
        else{
            return null;
        }
    }

    @Override
    public UserAddressInfo getUserAddressInfoById(Integer userAddressInfoId) {
        String sql = "SELECT user_address_info_id, user_id, receiver, contact, address " +
                "FROM user_address_info WHERE user_address_info_id = :userAddressInfoId";
        Map<String, Object> map = new HashMap<>();
        map.put("userAddressInfoId", userAddressInfoId);

        List<UserAddressInfo> infoList = namedParameterJdbcTemplate.query(sql, map, new UserAddressInfoRowMapper());

        if(infoList.size()>0) {
            return infoList.get(0);
        }
        else{
            return null;
        }
    }

    @Override
    public List<UserAddressInfo> getUserAddressInfoByUserId(Integer userId) {
        String sql = "SELECT user_address_info_id, user_id, receiver, contact, address " +
                "FROM user_address_info WHERE user_id = :userId";
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        List<UserAddressInfo> infoList = namedParameterJdbcTemplate.query(sql, map, new UserAddressInfoRowMapper());
        return infoList;
    }

    @Override
    public Integer createUserAddressInfo(UserAddressInfoRequest checkoutInfoRequest) {
        String sql = "INSERT INTO user_address_info(user_id, receiver, contact, address) " +
                "VALUES (:userId, :receiver, :contact, :address)";

        Map<String, Object> map = new HashMap<>();
        map.put("userId", checkoutInfoRequest.getUserId());
        map.put("receiver", checkoutInfoRequest.getReceiver());
        map.put("contact", checkoutInfoRequest.getContact());
        map.put("address", checkoutInfoRequest.getAddress());

        //取得資料庫自動產生的checkOutInfoId
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
        int userAddressInfoId = keyHolder.getKey().intValue();

        return userAddressInfoId;
    }
}
