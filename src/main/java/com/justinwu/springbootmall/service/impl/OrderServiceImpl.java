package com.justinwu.springbootmall.service.impl;

import com.justinwu.springbootmall.constant.OrderState;
import com.justinwu.springbootmall.dao.CartDao;
import com.justinwu.springbootmall.dao.OrderDao;
import com.justinwu.springbootmall.dao.ProductDao;
import com.justinwu.springbootmall.dao.UserDao;
import com.justinwu.springbootmall.dto.BuyItem;
import com.justinwu.springbootmall.dto.CreateOrderRequest;
import com.justinwu.springbootmall.dto.ECPayTrade;
import com.justinwu.springbootmall.dto.OrderQueryParams;
import com.justinwu.springbootmall.model.*;
import com.justinwu.springbootmall.service.OrderService;
import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class OrderServiceImpl implements OrderService {

    private final static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CartDao cartDao;

    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        return orderDao.countOrder(orderQueryParams);
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        List<Order> orderList = orderDao.getOrders(orderQueryParams);

        for(Order order : orderList){
            List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(order.getOrderId());

            order.setOrderItemList(orderItemList);
        }

        return orderList;
    }

    @Override
    public Order getOrderById(Integer orderId) {
        Order order = orderDao.getOrderById(orderId);

        List<OrderItem> orderItemList = orderDao.getOrderItemsByOrderId(orderId);

        order.setOrderItemList(orderItemList);

        return order;
    }

    @Transactional
    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        //檢查 user 是否存在
        User user = userDao.getUserById(userId);

        if (user == null){
            log.warn("user {} 不存在", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "用戶不存在");
        }

        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for(BuyItem buyItem : createOrderRequest.getBuyItemList()){
            Product product = productDao.getProductById(buyItem.getProductId());
            CartItem cartItem = cartDao.getCartItemById(buyItem.getCartItemId());

            //檢查 cartItem 是否存在
            if( cartItem == null){
                log.warn("購物車商品 {} 不存在", buyItem.getCartItemId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "購物車商品不存在");
            }

            //檢查 product 是否存在、庫存是否足夠
            if (product == null){
                log.warn("商品 {} 不存在", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "商品不存在");
            }else if(product.getStock() < buyItem.getQuantity()){
                log.warn("商品 {} 庫存數量不足，無法購買。剩餘庫存 {}，欲購買數量 {}",
                        buyItem.getProductId(), product.getStock(), buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "商品庫存數量不足，無法購買");
            }

            //移除購物車商品
            cartDao.deleteCartItemById(cartItem.getCartItemId());

            //扣除商品庫存
            productDao.updateStock(product.getProductId(), product.getStock() - buyItem.getQuantity());

            //計算總金額
            int amount = buyItem.getQuantity() * product.getPrice();
            totalAmount += amount;

            //轉換 BuyItem to OrderItem;
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItemList.add(orderItem);
        }

        //創建訂單
        Integer orderId = orderDao.createOrder(userId, totalAmount, createOrderRequest);

        orderDao.createOrderItems(orderId, orderItemList);

        return  orderId;
    }

    @Override
    public String ecpayCheckout(Integer orderId) {

        String tradeNo = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
        orderDao.createEcpayTrade(orderId, tradeNo);

        Order order = orderDao.getOrderById(orderId);
        Integer totalAmount = order.getTotalAmount();

        AllInOne all = new AllInOne("");

        AioCheckOutALL obj = new AioCheckOutALL();
        obj.setMerchantTradeNo(tradeNo);

        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formattedDate = formatter.format(now);
        obj.setMerchantTradeDate(formattedDate);

        obj.setTotalAmount(String.valueOf(totalAmount));
        obj.setTradeDesc("test Description");
        obj.setItemName("TestItem");
        obj.setReturnURL("https://justinwuproject.online/mall/api/ecpayResult");
        obj.setNeedExtraPaidInfo("N");
        obj.setClientBackURL("https://justinwuproject.online/mall/");
        String form = all.aioCheckOut(obj, null);

        return form;
    }

    @Override
    public ECPayTrade getEcpayTradeByTradeNo(String tradeNo) {
        return orderDao.getEcpayTradeByTradeNo(tradeNo);
    }

    @Override
    public void updateOrderState(Integer orderId, OrderState orderState) {
        orderDao.updateOrderState(orderId, orderState);
    }
}
