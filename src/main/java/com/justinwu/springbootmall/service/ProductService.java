package com.justinwu.springbootmall.service;

import com.justinwu.springbootmall.dto.ProductRequest;
import com.justinwu.springbootmall.model.Product;

public interface ProductService {
    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productId, ProductRequest productRequest);
    void deleteProductById(Integer productId);
}
