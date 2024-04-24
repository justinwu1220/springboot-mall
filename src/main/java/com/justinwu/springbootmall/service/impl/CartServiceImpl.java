package com.justinwu.springbootmall.service.impl;

import com.justinwu.springbootmall.dao.CartDao;
import com.justinwu.springbootmall.dao.UserDao;
import com.justinwu.springbootmall.dto.CartItemRequest;
import com.justinwu.springbootmall.model.CartItem;
import com.justinwu.springbootmall.model.User;
import com.justinwu.springbootmall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class CartServiceImpl implements CartService {

    @Autowired
    private CartDao cartDao;

    @Autowired
    private UserDao userDao;

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
    public Integer createCartItem(Integer userId, CartItemRequest cartItemRequest) {
        if(!isUserExist(userId)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "用戶不存在");
        }

        //新增購物車商品
        Integer cartItemId = cartDao.createCartItem(userId, cartItemRequest);

        return cartItemId;
    }

    @Override
    public void updateCartItem(Integer userId, Integer cartItemId, CartItemRequest cartItemRequest) {
        if(!isUserExist(userId)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "用戶不存在");
        }
        else if(!isBelong(userId,cartItemId)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "該用戶無此購物車商品");
        }

        //更新購物車商品
        cartDao.updateCartItem(userId, cartItemId, cartItemRequest);
    }

    @Override
    public void deleteCartItemById(Integer userId, Integer cartItemId) {
        if(!isUserExist(userId)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "用戶不存在");
        }
        else if(!isBelong(userId,cartItemId)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "該用戶無此購物車商品");
        }

        cartDao.deleteCartItemById(cartItemId);
    }

    @Override
    public Boolean isUserExist(Integer userId){
        //檢查 user 是否存在
        User user = userDao.getUserById(userId);

        if (user == null){
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public Boolean isBelong(Integer userId, Integer cartItemId){
        CartItem cartItem = cartDao.getCartItemById(cartItemId);
        if(cartItem.getUserId().equals(userId)){
            return true;
        }
        else{
            return false;
        }
    }
}
