package com.justinwu.springbootmall.service.impl;

import com.justinwu.springbootmall.dao.ProductDao;
import com.justinwu.springbootmall.model.Product;
import com.justinwu.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;
    @Override
    public Product getProductById(Integer productId) {

        return productDao.getProductById(productId);
    }
}
