package com.justinwu.springbootmall.rowmapper;

import com.justinwu.springbootmall.constant.OrderState;
import com.justinwu.springbootmall.constant.ProductCategory;
import com.justinwu.springbootmall.model.Order;
import com.justinwu.springbootmall.model.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

//將資料庫取出的數據轉換成java object
public class OrderRowMapper implements RowMapper<Order> {
    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setUserId(rs.getInt("user_id"));
        order.setTotalAmount(rs.getInt("total_amount"));
        order.setState(OrderState.valueOf(rs.getString("state")));
        order.setCreatedDate(rs.getTimestamp("created_date"));
        order.setLastModifiedDate(rs.getTimestamp("last_modified_date"));
        order.setReceiver(rs.getString("receiver"));
        order.setContact(rs.getString("contact"));
        order.setAddress(rs.getString("address"));

        return order;
    }
}
