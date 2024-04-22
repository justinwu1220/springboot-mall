package com.justinwu.springbootmall.rowmapper;

import com.justinwu.springbootmall.model.UserAddressInfo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAddressInfoRowMapper implements RowMapper<UserAddressInfo> {
    @Override
    public UserAddressInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserAddressInfo userAddressInfo = new UserAddressInfo();

        userAddressInfo.setUserAddressInfoId(rs.getInt("user_address_info_id"));
        userAddressInfo.setUserId(rs.getInt("user_id"));
        userAddressInfo.setReceiver(rs.getString("receiver"));
        userAddressInfo.setContact(rs.getString("contact"));
        userAddressInfo.setAddress(rs.getString("address"));

        return userAddressInfo;
    }
}
