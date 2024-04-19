package com.justinwu.springbootmall.rowmapper;

import com.justinwu.springbootmall.model.CartItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CartItemRowMapper implements RowMapper<CartItem> {

    @Override
    public CartItem mapRow(ResultSet rs, int rowNum) throws SQLException {

        CartItem cartItem = new CartItem();
        cartItem.setCartItemId(rs.getInt("cart_item_id"));
        cartItem.setUserId(rs.getInt("user_id"));
        cartItem.setProductId(rs.getInt("product_id"));
        cartItem.setProductName(rs.getString("product_name"));
        cartItem.setImageUrl(rs.getString("image_url"));
        cartItem.setQuantity(rs.getInt("quantity"));

        return cartItem;
    }
}
