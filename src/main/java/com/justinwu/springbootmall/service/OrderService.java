package com.justinwu.springbootmall.service;

import com.justinwu.springbootmall.dto.CreateOrderRequest;
import com.justinwu.springbootmall.model.Order;

public interface OrderService {

    Order getOrderById(Integer orderId);
    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
}
