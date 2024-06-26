package com.justinwu.springbootmall.dao.impl;

import com.justinwu.springbootmall.dao.ProductDao;
import com.justinwu.springbootmall.dto.ProductQueryParams;
import com.justinwu.springbootmall.dto.ProductRequest;
import com.justinwu.springbootmall.model.Product;
import com.justinwu.springbootmall.model.ProductOtherImage;
import com.justinwu.springbootmall.rowmapper.ProductOtherImagesRowMapper;
import com.justinwu.springbootmall.rowmapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class ProductDaoImpl implements ProductDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer countProduct(ProductQueryParams productQueryParams) {
        String sql = "SELECT count(*) From product WHERE 1=1";
        Map<String, Object> map = new HashMap<>();
        sql = addFilteringSql(sql, map, productQueryParams);
        Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
        return total;
    }

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {
        String sql = "SELECT product_id, product_name, category, image_url, price," +
                " stock, description, created_date, last_modified_date" +
                " FROM product WHERE 1=1";  //用where 1=1 不會影響原sql結果，也可以讓其他查詢條件sql方便拼接在後面

        Map<String, Object> map = new HashMap<>();
        //查詢條件
        sql = addFilteringSql(sql, map, productQueryParams);
        //排序
        sql = sql + " ORDER BY " + productQueryParams.getOrderBy() + " " + productQueryParams.getSort();
        //分頁
        sql = sql + " LIMIT :limit OFFSET :offset";
        map.put("limit", productQueryParams.getLimit());
        map.put("offset", productQueryParams.getOffset());

        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
        return productList;
    }

    @Override
    public Product getProductById(Integer productId) {
        String sql = "SELECT product_id, product_name, category, image_url, price," +
                " stock, description, created_date, last_modified_date" +
                " FROM product WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        //namedParameterJdbcTemplate.query會返回list
        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
        //productList若為空，則返回null
        if(productList.size()>0) {
            return productList.get(0);
        }
        else{
            return null;
        }
    }

    @Override
    public List<ProductOtherImage> getProductOtherImagesById(Integer productId) {
        String sql = "SELECT product_id, image_url FROM product_other_images WHERE product_id = :productId";
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        //namedParameterJdbcTemplate.query會返回list
        List<ProductOtherImage> imageList = namedParameterJdbcTemplate.query(sql, map, new ProductOtherImagesRowMapper());
        return imageList;
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        String sql = "INSERT INTO product(product_name, category, image_url, price," +
                " stock, description, created_date, last_modified_date)" +
                " VALUES (:productName, :category, :imageUrl, :price, :stock, :description," +
                ":createdDate, :lastModifiedDate)";

        //用map將變數值帶入sql中
        Map<String, Object> map = new HashMap<>();
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        //取得資料庫自動產生的productId
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
        int productId = keyHolder.getKey().intValue();

        return productId;
    }

    @Override
    public void createProductOtherImages(Integer productId, List<String> otherImagesUrlList) {
        //使用batchUpdate加入數據
        String sql = "INSERT INTO product_other_images(product_id, image_url)" +
                " VALUES (:productId, :imageUrl)";

        MapSqlParameterSource[] parameterSources = new MapSqlParameterSource[otherImagesUrlList.size()];

        for (int i = 0; i < otherImagesUrlList.size(); i++){
            String imageUrl = otherImagesUrlList.get(i);

            parameterSources[i] = new MapSqlParameterSource();
            parameterSources[i].addValue("productId", productId);
            parameterSources[i].addValue("imageUrl", imageUrl);
        }

        namedParameterJdbcTemplate.batchUpdate(sql, parameterSources);
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        String sql = "UPDATE product SET product_name = :productName, category = :category, " +
                "image_url = :imageUrl, price = :price, stock = :stock, description = :description, " +
                "last_modified_date = :lastModifiedDate WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());
        map.put("lastModifiedDate", new Date());

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public void updateStock(Integer productId, Integer stock) {
        String sql = "UPDATE product SET stock = :stock, last_modified_date = :lastModifiedDate" +
                " WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        map.put("stock", stock);
        map.put("lastModifiedDate", new Date());

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public void deleteProductById(Integer productId) {
        String sql = "DELETE FROM product WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public void deleteProductOtherImagesById(Integer productId) {
        String sql = "DELETE FROM product_other_images WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        namedParameterJdbcTemplate.update(sql, map);
    }

    //查詢條件sql拼接
    private String addFilteringSql(String sql, Map<String, Object> map, ProductQueryParams productQueryParams){
        if(productQueryParams.getCategory() != null){
            sql = sql + " AND category = :category";
            map.put("category", productQueryParams.getCategory().toString());
        }
        if(productQueryParams.getSearch() != null){
            sql = sql + " AND product_name LIKE :search";
            map.put("search", "%" + productQueryParams.getSearch() + "%");
        }

        return sql;
    }
}
