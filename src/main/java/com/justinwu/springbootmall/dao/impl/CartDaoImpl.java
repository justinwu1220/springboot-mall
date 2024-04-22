package com.justinwu.springbootmall.dao.impl;

import com.justinwu.springbootmall.dao.CartDao;
import com.justinwu.springbootmall.dto.CartItemRequest;
import com.justinwu.springbootmall.model.CartItem;
import com.justinwu.springbootmall.rowmapper.CartItemRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CartDaoImpl implements CartDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<CartItem> getCartByUserId(Integer userId) {
        String sql = "SELECT ci.cart_item_id, u.user_id, p.product_id," +
                "p.product_name, p.image_url, p.price, ci.quantity " +
                "FROM cart_item AS ci " +
                "INNER JOIN " +
                "product AS p ON ci.product_id = p.product_id " +
                "INNER JOIN " +
                "user AS u ON ci.user_id = u.user_id " +
                "WHERE u.user_id = :userId;";

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        List<CartItem> cart = namedParameterJdbcTemplate.query(sql, map , new CartItemRowMapper());

        return cart;
    }

    @Override
    public CartItem getCartItemById(Integer cartItemId) {
        String sql = "SELECT ci.cart_item_id, u.user_id, p.product_id," +
                "p.product_name, p.image_url, p.price, ci.quantity " +
                "FROM cart_item AS ci " +
                "INNER JOIN " +
                "product AS p ON ci.product_id = p.product_id " +
                "INNER JOIN " +
                "user AS u ON ci.user_id = u.user_id " +
                "WHERE ci.cart_item_id = :cartItemId";

        Map<String, Object> map = new HashMap<>();
        map.put("cartItemId", cartItemId);

        //namedParameterJdbcTemplate.query會返回list
        List<CartItem> cartItemList = namedParameterJdbcTemplate.query(sql, map , new CartItemRowMapper());
        //cartItemList若為空，則返回null
        if(cartItemList.size()>0) {
            return cartItemList.get(0);
        }
        else{
            return null;
        }
    }

    @Override
    public Integer createCartItem(CartItemRequest cartItemRequest) {
        String sql = "INSERT INTO cart_item(user_id,product_id,quantity) " +
                "VALUES (:userId, :productId, :quantity)";

        //用map將變數值帶入sql中
        Map<String, Object> map = new HashMap<>();
        map.put("userId", cartItemRequest.getUserId());
        map.put("productId", cartItemRequest.getProductId());
        map.put("quantity", cartItemRequest.getQuantity());

        //取得資料庫自動產生的cartItemId
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
        int cartItemId = keyHolder.getKey().intValue();

        return cartItemId;
    }

    @Override
    public void updateCartItem(Integer cartItemId, CartItemRequest cartItemRequest) {
        String sql = "UPDATE cart_item SET user_id = :userId," +
                "product_id = :productId, quantity = :quantity " +
                "WHERE cart_item_id = :cartItemId";

        Map<String, Object> map = new HashMap<>();
        map.put("cartItemId", cartItemId);
        map.put("userId", cartItemRequest.getUserId());
        map.put("productId", cartItemRequest.getProductId());
        map.put("quantity", cartItemRequest.getQuantity());

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public void deleteCartItemById(Integer cartItemId) {
        String sql = "DELETE FROM cart_item WHERE cart_item_id = :cartItemId";

        Map<String, Object> map = new HashMap<>();
        map.put("cartItemId", cartItemId);

        namedParameterJdbcTemplate.update(sql, map);
    }
}
