package com.justinwu.springbootmall.model;

public class UserAddressInfo {
    private Integer userAddressInfoId;
    private Integer userId;
    private String receiver;
    private String contact;
    private String address;

    public Integer getUserAddressInfoId() {
        return userAddressInfoId;
    }

    public void setUserAddressInfoId(Integer userAddressInfoId) {
        this.userAddressInfoId = userAddressInfoId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
