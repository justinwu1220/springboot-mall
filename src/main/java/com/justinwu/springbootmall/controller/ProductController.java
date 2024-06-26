package com.justinwu.springbootmall.controller;

import com.justinwu.springbootmall.constant.ProductCategory;
import com.justinwu.springbootmall.dto.ProductQueryParams;
import com.justinwu.springbootmall.dto.ProductRequest;
import com.justinwu.springbootmall.model.Product;
import com.justinwu.springbootmall.service.ProductService;
import com.justinwu.springbootmall.tool.AdminAuthorityCheck;
import com.justinwu.springbootmall.tool.PassToken;
import com.justinwu.springbootmall.tool.UserLoginToken;
import com.justinwu.springbootmall.util.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @PassToken//不需登入權限
    @GetMapping("/products")//查詢商品列表
    public  ResponseEntity<Page<Product>> getProducts(
            //查詢條件 Filtering
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) String search,

            //排序 Sorting
            @RequestParam(defaultValue = "last_modified_date") String orderBy,
            @RequestParam(defaultValue = "desc") String sort,

            //分頁 Pagination
            @RequestParam(defaultValue = "1000") @Max(1000) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset
    ){
        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);
        productQueryParams.setLimit(limit);
        productQueryParams.setOffset(offset);

        //查詢條件取得的商品總數(不受limit、offset 影響)
        Integer total = productService.countProduct(productQueryParams);

        //分頁取得的商品列表(受到limit、offset 影響)
        List<Product> productList = productService.getProducts(productQueryParams);

        Page<Product> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        page.setResult(productList);

        //查詢 列表 時無論結果有無數據，都需回傳200 OK(RESTful 設計理念)
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    @PassToken//不需登入權限
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

    @AdminAuthorityCheck//需要Admin權限
    @PostMapping("/products")//建立商品
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest){
        //建立商品後返回自動增加的productId
        Integer productId = productService.createProduct(productRequest);
        //透過productId查詢建立好的商品並返回給前端
        Product product = productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @AdminAuthorityCheck//需要Admin權限
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

    @AdminAuthorityCheck//需要Admin權限
    @DeleteMapping("/products/{productId}")//刪除商品
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId){
        //不檢查商品是否存在，因為無論商品原本是否存在，最後結果都要是不存在
        productService.deleteProductById(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
