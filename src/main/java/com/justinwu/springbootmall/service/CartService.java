package com.justinwu.springbootmall.service;

import com.justinwu.springbootmall.dto.CartItemRequest;
import com.justinwu.springbootmall.model.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;

public interface CartService {

    List<CartItem> getCartByUserId(Integer userId);
    CartItem getCartItemById(Integer cartItemId);
    Integer createCartItem(Integer userId, CartItemRequest cartItemRequest);
    void updateCartItem(Integer userId, Integer cartItemId, CartItemRequest cartItemRequest);
    void deleteCartItemById(Integer userId, Integer cartItemId);
    Boolean isUserExist(Integer userId);
    Boolean isBelong(Integer userId, Integer cartItemId);
}
