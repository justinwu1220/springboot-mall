package com.justinwu.springbootmall.service.impl;

import com.justinwu.springbootmall.dao.CartDao;
import com.justinwu.springbootmall.dto.CartItemRequest;
import com.justinwu.springbootmall.model.CartItem;
import com.justinwu.springbootmall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartServiceImpl implements CartService {

    @Autowired
    private CartDao cartDao;

    @Override
    public List<CartItem> getCartByUserId(Integer userId) {
        return cartDao.getCartByUserId(userId);
    }

    @Override
    public CartItem getCartItemById(Integer cartItemId) {
        CartItem cartItem = cartDao.getCartItemById(cartItemId);

        return cartItem;
    }

    @Override
    public Integer createCartItem(CartItemRequest cartItemRequest) {
        //新增購物車商品
        Integer cartItemId = cartDao.createCartItem(cartItemRequest);

        return cartItemId;
    }

    @Override
    public void updateCartItem(Integer cartItemId, CartItemRequest cartItemRequest) {
        //更新購物車商品
        cartDao.updateCartItem(cartItemId, cartItemRequest);
    }

    @Override
    public void deleteCartItemById(Integer cartItemId) {
        cartDao.deleteCartItemById(cartItemId);
    }
}
