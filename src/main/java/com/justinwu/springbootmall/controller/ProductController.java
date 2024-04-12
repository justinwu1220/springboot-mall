package com.justinwu.springbootmall.controller;

import com.justinwu.springbootmall.dto.ProductRequest;
import com.justinwu.springbootmall.model.Product;
import com.justinwu.springbootmall.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;
    @GetMapping("/products/{productId}")//透過productId查詢商品
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId){
        //透過productId查詢商品
        Product product = productService.getProductById(productId);
        //若商品存在，返回商品給前端; 若不存在，返回404not found 給前端
        if(product != null){
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }
        else {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/products")//建立商品
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest){
        //建立商品後返回自動增加的productId
        Integer productId = productService.createProduct(productRequest);
        //透過productId查詢建立好的商品並返回給前端
        Product product = productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/products/{productId}")//更新商品
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
                                                 @RequestBody @Valid ProductRequest productRequest){

        //先查詢商品是否存在，若不存在則返回 404not found 給前端
        Product product = productService.getProductById(productId);
        if(product == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        //若商品存在則更新商品數據，將更新後的商品返回給前端
        productService.updateProduct(productId, productRequest);
        Product updatedProduct = productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);
    }
}
