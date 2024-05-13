package com.justinwu.springbootmall.rowmapper;

import com.justinwu.springbootmall.dto.ECPayTrade;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ECPayTradeRowMapper implements RowMapper<ECPayTrade> {
    @Override
    public ECPayTrade mapRow(ResultSet rs, int rowNum) throws SQLException {
        ECPayTrade ecPayTrade = new ECPayTrade();
        ecPayTrade.setTradeNo(rs.getString("trade_no"));
        ecPayTrade.setOrderId(rs.getInt("order_id"));

        return ecPayTrade;
    }
}
