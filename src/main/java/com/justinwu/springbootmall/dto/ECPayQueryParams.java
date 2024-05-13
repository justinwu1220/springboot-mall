package com.justinwu.springbootmall.dto;

public class ECPayQueryParams {
    private String MerchantID;
    private String MerchantTradeNo;
    private String StoreID;
    private Integer RtnCode;
    private String RtnMsg;
    private String TradeNo;
    private Integer TradeAmt;
    private String PaymentDate;
    private String PaymentType;
    private Integer PaymentTypeChargeFee;
    private String TradeDate;
    private String SimulatePaid;
    private String CheckMacValue;

    public String getMerchantID() {
        return MerchantID;
    }

    public void setMerchantID(String merchantID) {
        MerchantID = merchantID;
    }

    public String getMerchantTradeNo() {
        return MerchantTradeNo;
    }

    public void setMerchantTradeNo(String merchantTradeNo) {
        MerchantTradeNo = merchantTradeNo;
    }

    public String getStoreID() {
        return StoreID;
    }

    public void setStoreID(String storeID) {
        StoreID = storeID;
    }

    public Integer getRtnCode() {
        return RtnCode;
    }

    public void setRtnCode(Integer rtnCode) {
        RtnCode = rtnCode;
    }

    public String getRtnMsg() {
        return RtnMsg;
    }

    public void setRtnMsg(String rtnMsg) {
        RtnMsg = rtnMsg;
    }

    public String getTradeNo() {
        return TradeNo;
    }

    public void setTradeNo(String tradeNo) {
        TradeNo = tradeNo;
    }

    public Integer getTradeAmt() {
        return TradeAmt;
    }

    public void setTradeAmt(Integer tradeAmt) {
        TradeAmt = tradeAmt;
    }

    public String getPaymentDate() {
        return PaymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        PaymentDate = paymentDate;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }

    public Integer getPaymentTypeChargeFee() {
        return PaymentTypeChargeFee;
    }

    public void setPaymentTypeChargeFee(Integer paymentTypeChargeFee) {
        PaymentTypeChargeFee = paymentTypeChargeFee;
    }

    public String getTradeDate() {
        return TradeDate;
    }

    public void setTradeDate(String tradeDate) {
        TradeDate = tradeDate;
    }

    public String getSimulatePaid() {
        return SimulatePaid;
    }

    public void setSimulatePaid(String simulatePaid) {
        SimulatePaid = simulatePaid;
    }

    public String getCheckMacValue() {
        return CheckMacValue;
    }

    public void setCheckMacValue(String checkMacValue) {
        CheckMacValue = checkMacValue;
    }
}
