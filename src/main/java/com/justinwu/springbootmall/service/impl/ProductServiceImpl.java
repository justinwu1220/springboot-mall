package com.justinwu.springbootmall.service.impl;

import com.justinwu.springbootmall.dao.ProductDao;
import com.justinwu.springbootmall.dto.ProductQueryParams;
import com.justinwu.springbootmall.dto.ProductRequest;
import com.justinwu.springbootmall.model.Product;
import com.justinwu.springbootmall.model.ProductOtherImage;
import com.justinwu.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public Integer countProduct(ProductQueryParams productQueryParams) {
        return productDao.countProduct(productQueryParams);
    }

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {
        return productDao.getProducts(productQueryParams);
    }

    @Override
    public Product getProductById(Integer productId) {

        Product product = productDao.getProductById(productId);
        List<ProductOtherImage> otherImagesList = productDao.getProductOtherImagesById(productId);
        List<String> imageList = new ArrayList<>();
        for(ProductOtherImage image : otherImagesList){
            imageList.add(image.getImageUrl());
        }
        product.setOtherImagesUrl(imageList);

        return product;
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        //取得接口傳過來的productOtherImages
        List<String> otherImagesUrlList = productRequest.getOtherImagesUrl();
        //新增商品
        Integer productId = productDao.createProduct(productRequest);
        //新增productOtherImages
        productDao.createProductOtherImages(productId, otherImagesUrlList);
        return productId;
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        //更新商品
        productDao.updateProduct(productId, productRequest);
        //更新productOtherImages
        //由於product other images表較特殊，因此先刪後建立，而不使用update//可以再優化
        List<String> otherImagesUrlList = productRequest.getOtherImagesUrl();
        productDao.deleteProductOtherImagesById(productId);
        productDao.createProductOtherImages(productId, otherImagesUrlList);
    }

    @Override
    public void deleteProductById(Integer productId) {
        productDao.deleteProductOtherImagesById(productId);
        productDao.deleteProductById(productId);
    }
}
