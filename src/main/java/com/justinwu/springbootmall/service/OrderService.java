package com.justinwu.springbootmall.service;

import com.justinwu.springbootmall.constant.OrderState;
import com.justinwu.springbootmall.dto.CreateOrderRequest;
import com.justinwu.springbootmall.dto.ECPayTrade;
import com.justinwu.springbootmall.dto.OrderQueryParams;
import com.justinwu.springbootmall.model.Order;

import java.util.List;

public interface OrderService {
    Integer countOrder(OrderQueryParams orderQueryParams);
    List<Order> getOrders(OrderQueryParams orderQueryParams);
    Order getOrderById(Integer orderId);
    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);
    String ecpayCheckout(Integer orderId);
    ECPayTrade getEcpayTradeByTradeNo(String tradeNo);
    void updateOrderState(Integer orderId, OrderState orderState);
}
