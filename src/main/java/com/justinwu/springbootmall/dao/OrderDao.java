package com.justinwu.springbootmall.dao;

import com.justinwu.springbootmall.constant.OrderState;
import com.justinwu.springbootmall.dto.CreateOrderRequest;
import com.justinwu.springbootmall.dto.ECPayTrade;
import com.justinwu.springbootmall.dto.OrderQueryParams;
import com.justinwu.springbootmall.model.Order;
import com.justinwu.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderDao {

    Integer countOrder(OrderQueryParams orderQueryParams);
    List<Order> getOrders(OrderQueryParams orderQueryParams);
    Order getOrderById(Integer orderId);
    List<OrderItem> getOrderItemsByOrderId(Integer orderId);
    Integer createOrder(Integer userId, Integer totalAmount, CreateOrderRequest createOrderRequest);
    void createOrderItems(Integer orderId, List<OrderItem> orderItemList);
    void createEcpayTrade(Integer orderId, String tradeNo);
    ECPayTrade getEcpayTradeByTradeNo(String tradeNo);
    void updateOrderState(Integer orderId, OrderState orderState);
}
