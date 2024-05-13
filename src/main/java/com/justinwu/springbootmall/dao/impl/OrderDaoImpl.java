package com.justinwu.springbootmall.dao.impl;

import com.justinwu.springbootmall.constant.OrderState;
import com.justinwu.springbootmall.dao.OrderDao;
import com.justinwu.springbootmall.dto.CreateOrderRequest;
import com.justinwu.springbootmall.dto.ECPayTrade;
import com.justinwu.springbootmall.dto.OrderQueryParams;
import com.justinwu.springbootmall.dto.ProductQueryParams;
import com.justinwu.springbootmall.model.Order;
import com.justinwu.springbootmall.model.OrderItem;
import com.justinwu.springbootmall.model.Product;
import com.justinwu.springbootmall.rowmapper.ECPayTradeRowMapper;
import com.justinwu.springbootmall.rowmapper.OrderItemRowMapper;
import com.justinwu.springbootmall.rowmapper.OrderRowMapper;
import com.justinwu.springbootmall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDaoImpl implements OrderDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        String sql = "SELECT count(*) FROM `order` WHERE 1=1";
        Map<String, Object> map = new HashMap<>();

        //查詢條件
        sql = addFilteringSql(sql, map, orderQueryParams);

        Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);

        return total;
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        String sql = "SELECT order_id, user_id, total_amount, state," +
                "created_date, last_modified_date, receiver, contact, address " +
                "FROM `order` WHERE 1=1";
        Map<String, Object> map = new HashMap<>();

        //查詢條件
        sql = addFilteringSql(sql, map, orderQueryParams);
        //排序
        sql = sql + " ORDER BY created_date DESC";
        //分頁
        sql = sql + " LIMIT :limit OFFSET :offset";
        map.put("limit", orderQueryParams.getLimit());
        map.put("offset", orderQueryParams.getOffset());

        List<Order> orderList = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());

        return orderList;
    }

    @Override
    public Order getOrderById(Integer orderId) {
        String sql = "SELECT order_id, user_id, total_amount, state," +
                "created_date, last_modified_date, receiver, contact, address " +
                "FROM `order` WHERE order_id = :orderId";

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        //namedParameterJdbcTemplate.query會返回list
        List<Order> orderList = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());
        //orderList若為空，則返回null
        if(orderList.size()>0) {
            return orderList.get(0);
        }
        else{
            return null;
        }
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {
        String sql = "SELECT oi.order_item_id, oi.order_id, oi.product_id, oi.quantity, oi.amount, p.product_name, p.image_url" +
                " FROM order_item as oi" +
                " LEFT JOIN product as p ON oi.product_id = p.product_id" +
                " WHERE oi.order_id = :orderId";

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);

        List<OrderItem> orderItemList = namedParameterJdbcTemplate.query(sql, map, new OrderItemRowMapper());

        return orderItemList;
    }

    @Override
    public Integer createOrder(Integer userId, Integer totalAmount, CreateOrderRequest createOrderRequest) {
        String sql = "INSERT INTO `order`(user_id, total_amount, state, created_date, last_modified_date," +
                "receiver, contact, address) " +
                "VALUES (:userId, :totalAmount, :state, :createdDate, :lastModifiedDate, :receiver, :contact, :address)";

        //用map將變數值帶入sql中
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("totalAmount", totalAmount);
        map.put("receiver", createOrderRequest.getReceiver());
        map.put("contact", createOrderRequest.getContact());
        map.put("address", createOrderRequest.getAddress());

        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        map.put("state", OrderState.UNPAID.toString());

        //取得資料庫自動產生的productId
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
        int orderId = keyHolder.getKey().intValue();

        return orderId;
    }

    @Override
    public void createOrderItems(Integer orderId, List<OrderItem> orderItemList) {
        //使用batchUpdate加入數據
        String sql = "INSERT INTO order_item(order_id, product_id, quantity, amount)" +
                " VALUES (:orderId, :productId, :quantity, :amount)";

        MapSqlParameterSource[] parameterSources = new MapSqlParameterSource[orderItemList.size()];

        for (int i = 0; i < orderItemList.size(); i++){
            OrderItem orderItem = orderItemList.get(i);

            parameterSources[i] = new MapSqlParameterSource();
            parameterSources[i].addValue("orderId", orderId);
            parameterSources[i].addValue("productId", orderItem.getProductId());
            parameterSources[i].addValue("quantity", orderItem.getQuantity());
            parameterSources[i].addValue("amount", orderItem.getAmount());
        }

        namedParameterJdbcTemplate.batchUpdate(sql, parameterSources);
    }

    @Override
    public void createEcpayTrade(Integer orderId, String tradeNo) {
        String sql = "INSERT INTO ecpay_trade(order_id, trade_no) " +
                "VALUES (:orderId, :tradeNo)";

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("tradeNo", tradeNo);

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public ECPayTrade getEcpayTradeByTradeNo(String tradeNo) {
        String sql = "SELECT trade_no, order_id " +
                " FROM ecpay_trade WHERE trade_no = :tradeNo";

        Map<String, Object> map = new HashMap<>();
        map.put("tradeNo", tradeNo);

        List<ECPayTrade> tradeList = namedParameterJdbcTemplate.query(sql, map, new ECPayTradeRowMapper());
        if(tradeList.size()>0) {
            return tradeList.get(0);
        }
        else{
            return null;
        }
    }

    @Override
    public void updateOrderState(Integer orderId, OrderState orderState) {
        String sql = "UPDATE `order` SET state = :orderState " +
                "WHERE order_id = :orderId";

        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("orderState", orderState.toString());

        namedParameterJdbcTemplate.update(sql, map);
    }

    private String addFilteringSql(String sql, Map<String, Object> map, OrderQueryParams orderQueryParams){
        if(orderQueryParams.getUserId() != null){
            sql = sql + " AND user_id = :userId";
            map.put("userId", orderQueryParams.getUserId());
        }
        if(orderQueryParams.getState() != null){
            sql = sql + " AND state = :state";
            map.put("state", orderQueryParams.getState().toString());
        }
        return sql;
    }
}
