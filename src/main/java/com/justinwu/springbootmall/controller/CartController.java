package com.justinwu.springbootmall.controller;

import com.justinwu.springbootmall.dto.CartItemRequest;
import com.justinwu.springbootmall.model.CartItem;
import com.justinwu.springbootmall.model.Product;
import com.justinwu.springbootmall.service.CartService;
import com.justinwu.springbootmall.tool.UserLoginToken;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @UserLoginToken//需要登入權限
    @GetMapping("/users/{userId}/cart")
    public ResponseEntity<List<CartItem>> getCart(@PathVariable Integer userId){
        //取得user的cart
        List<CartItem> cart = cartService.getCartByUserId(userId);
        //返回購物車，無論購物車內有無商品
        return ResponseEntity.status(HttpStatus.OK).body(cart);
    }

    @UserLoginToken//需要登入權限
    @PostMapping("/users/{userId}/cart")
    public ResponseEntity<CartItem> createCartItem(@RequestBody @Valid CartItemRequest cartItemRequest){
        //建立商品後返回自動增加的cartItemId
        Integer cartItemId = cartService.createCartItem(cartItemRequest);
        //透過cartItemId查詢建立好的購物車商品並返回給前端
        CartItem cartItem = cartService.getCartItemById(cartItemId);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItem);
    }

    @UserLoginToken//需要登入權限
    @PutMapping("/users/{userId}/cart/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItem(@PathVariable Integer cartItemId,
                                                   @RequestBody @Valid CartItemRequest cartItemRequest){
        //先查詢購物車商品是否存在，若不存在則返回 404not found 給前端
        CartItem cartItem = cartService.getCartItemById(cartItemId);
        if(cartItem == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        //若購物車商品存在則更新商品數據，將更新後的商品返回給前端
        cartService.updateCartItem(cartItemId, cartItemRequest);
        CartItem updatedCartItem = cartService.getCartItemById(cartItemId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCartItem);
    }

    @UserLoginToken//需要登入權限
    @DeleteMapping("/users/{userId}/cart/{cartItemId}")
    public ResponseEntity<?> deleteCartItem(
            @PathVariable Integer userId,
            @PathVariable Integer cartItemId){
        ////不檢查購物車商品是否存在，因為無論購物車商品原本是否存在，最後結果都要是不存在
        cartService.deleteCartItemById(cartItemId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
