package com.justinwu.springbootmall.dao;

import com.justinwu.springbootmall.dto.ProductQueryParams;
import com.justinwu.springbootmall.dto.ProductRequest;
import com.justinwu.springbootmall.model.Product;
import com.justinwu.springbootmall.model.ProductOtherImage;

import java.util.List;

public interface ProductDao {
    Integer countProduct(ProductQueryParams productQueryParams);
    List<Product> getProducts(ProductQueryParams productQueryParams);
    Product getProductById(Integer productId);
    List<ProductOtherImage> getProductOtherImagesById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    void createProductOtherImages(Integer productId, List<String> otherImagesUrlList);
    void updateProduct(Integer productId, ProductRequest productRequest);
    void updateStock(Integer productId, Integer stock);
    void deleteProductById(Integer productId);
    void deleteProductOtherImagesById(Integer productId);
}
