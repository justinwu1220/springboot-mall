package com.justinwu.springbootmall.dto;

import jakarta.validation.constraints.NotNull;

public class BuyItem {
    @NotNull
    private  Integer cartItemId;
    @NotNull
    private Integer productId;
    @NotNull
    private Integer quantity;

    public Integer getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Integer cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
