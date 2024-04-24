package com.justinwu.springbootmall.dto;

import jakarta.validation.constraints.NotNull;

public class CartItemRequest {
    @NotNull
    private Integer productId;
    @NotNull
    private Integer quantity;
    @NotNull
    private Boolean selected;


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

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
