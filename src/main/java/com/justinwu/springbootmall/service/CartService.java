package com.justinwu.springbootmall.service;

import com.justinwu.springbootmall.dto.CartItemRequest;
import com.justinwu.springbootmall.model.CartItem;
import org.springframework.stereotype.Component;

import java.util.List;

public interface CartService {

    List<CartItem> getCartByUserId(Integer userId);
    CartItem getCartItemById(Integer cartItemId);
    Integer createCartItem(CartItemRequest cartItemRequest);
    void updateCartItem(Integer cartItemId, CartItemRequest cartItemRequest);
    void deleteCartItemById(Integer cartItemId);
}
