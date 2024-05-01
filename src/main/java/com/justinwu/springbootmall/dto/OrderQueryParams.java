package com.justinwu.springbootmall.dto;

import com.justinwu.springbootmall.constant.OrderState;

public class OrderQueryParams {
    private Integer userId;
    private Integer limit;
    private Integer offset;
    private OrderState state;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }
}
