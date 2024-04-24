package com.justinwu.springbootmall.dao;

import com.justinwu.springbootmall.dto.CartItemRequest;
import com.justinwu.springbootmall.model.CartItem;

import java.util.List;

public interface CartDao {
    List<CartItem> getCartByUserId(Integer userId);
    CartItem getCartItemById(Integer cartItemId);
    Integer createCartItem(Integer userId, CartItemRequest cartItemRequest);
    void updateCartItem(Integer userId, Integer cartItemId, CartItemRequest cartItemRequest);
    void deleteCartItemById(Integer cartItemId);
}
