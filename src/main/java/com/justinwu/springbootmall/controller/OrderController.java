package com.justinwu.springbootmall.controller;

import com.justinwu.springbootmall.constant.Authority;
import com.justinwu.springbootmall.constant.OrderState;
import com.justinwu.springbootmall.constant.ProductCategory;
import com.justinwu.springbootmall.dto.CreateOrderRequest;
import com.justinwu.springbootmall.dto.ECPayQueryParams;
import com.justinwu.springbootmall.dto.OrderQueryParams;
import com.justinwu.springbootmall.model.Order;
import com.justinwu.springbootmall.model.User;
import com.justinwu.springbootmall.service.OrderService;
import com.justinwu.springbootmall.service.UserService;
import com.justinwu.springbootmall.tool.PassToken;
import com.justinwu.springbootmall.tool.UserLoginToken;
import com.justinwu.springbootmall.util.JwtUtil;
import com.justinwu.springbootmall.util.Page;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;

    @UserLoginToken//需要登入權限
    @GetMapping("/users/orders")
    public  ResponseEntity<Page<Order>> gerOrders(
            @RequestHeader("token") String token,
            @RequestParam(defaultValue = "10") @Max(1000) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset,
            @RequestParam(required = false)OrderState state
            ){
        // 獲取 token 中的 userId
        Claims claims = JwtUtil.parseJwtToken(token);
        Integer userId = Integer.valueOf(claims.getId());

        User user = userService.getUserById(userId);

        OrderQueryParams orderQueryParams = new OrderQueryParams();

        //若為CLIENT則找該CLIENT訂單，若為ADMIN則找全部訂單
        if(user.getAuthority().equals(Authority.CLIENT)){
            orderQueryParams.setUserId(userId);
        }
        orderQueryParams.setLimit(limit);
        orderQueryParams.setOffset(offset);
        orderQueryParams.setState(state);

        //取得 order list
        List<Order> orderList = orderService.getOrders(orderQueryParams);

        //取得 order 總數
        Integer total = orderService.countOrder(orderQueryParams);

        //分頁
        Page<Order> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        page.setResult(orderList);

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    @UserLoginToken//需要登入權限
    @PostMapping("/users/orders")
    public ResponseEntity<Order> createOrder(@RequestHeader("token") String token,
                                         @RequestBody @Valid CreateOrderRequest createOrderRequest){
        // 獲取 token 中的 userId
        Claims claims = JwtUtil.parseJwtToken(token);
        Integer userId = Integer.valueOf(claims.getId());

        Integer orderId = orderService.createOrder(userId, createOrderRequest);

        Order order = orderService.getOrderById(orderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PassToken
    @PostMapping("/ecpayCheckout/{orderId}")
    public String ecpayCheckout(@PathVariable Integer orderId) {
        String aioCheckOutALLForm = orderService.ecpayCheckout(orderId);

        return aioCheckOutALLForm;
    }

    @PassToken
    @PostMapping("/ecpayResult")
    public String ecpayResult(//查詢條件 Filtering
                              @RequestParam String MerchantID,
                              @RequestParam String MerchantTradeNo,
                              @RequestParam String CheckMacValue) {
        ECPayQueryParams ecPayQueryParams = new ECPayQueryParams();
        ecPayQueryParams.setMerchantID(MerchantID);
        ecPayQueryParams.setMerchantTradeNo(MerchantTradeNo);
        ecPayQueryParams.setCheckMacValue(CheckMacValue);

        Integer orderId = orderService.getEcpayTradeByTradeNo(MerchantTradeNo).getOrderId();
        orderService.updateOrderState(orderId, OrderState.COMPLETED);
        return "1|OK";
    }
}
