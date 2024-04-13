package com.justinwu.springbootmall.service;

import com.justinwu.springbootmall.dto.ProductQueryParams;
import com.justinwu.springbootmall.dto.ProductRequest;
import com.justinwu.springbootmall.model.Product;

import java.util.List;

public interface ProductService {

    Integer countProduct(ProductQueryParams productQueryParams);
    List<Product> getProducts(ProductQueryParams productQueryParams);
    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    void updateProduct(Integer productId, ProductRequest productRequest);
    void deleteProductById(Integer productId);
}
