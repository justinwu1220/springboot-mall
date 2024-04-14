package com.justinwu.springbootmall.service;

import com.justinwu.springbootmall.dto.CreateOrderRequest;
import com.justinwu.springbootmall.dto.OrderQueryParams;
import com.justinwu.springbootmall.model.Order;

import java.util.List;

public interface OrderService {
    Integer countOrder(OrderQueryParams orderQueryParams);
    List<Order> getOrders(OrderQueryParams orderQueryParams);
    Order getOrderById(Integer orderId);
    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
}
