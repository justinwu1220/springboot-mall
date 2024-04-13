package com.justinwu.springbootmall.dao;

import com.justinwu.springbootmall.dto.ProductQueryParams;
import com.justinwu.springbootmall.dto.ProductRequest;
import com.justinwu.springbootmall.model.Product;

import java.util.List;

public interface ProductDao {

    List<Product> getProducts(ProductQueryParams productQueryParams);
    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productId, ProductRequest productRequest);
    void deleteProductById(Integer productId);
}
