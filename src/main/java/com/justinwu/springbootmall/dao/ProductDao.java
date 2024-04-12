package com.justinwu.springbootmall.dao;

import com.justinwu.springbootmall.model.Product;

public interface ProductDao {

    Product getProductById(Integer productId);
}
