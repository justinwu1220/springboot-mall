package com.justinwu.springbootmall.service;

import com.justinwu.springbootmall.model.Product;

public interface ProductService {
    Product getProductById(Integer productId);
}
