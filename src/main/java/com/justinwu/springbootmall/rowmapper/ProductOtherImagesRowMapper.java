package com.justinwu.springbootmall.rowmapper;

import com.justinwu.springbootmall.model.ProductOtherImage;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductOtherImagesRowMapper implements RowMapper<ProductOtherImage> {

    @Override
    public ProductOtherImage mapRow(ResultSet rs, int rowNum) throws SQLException {
        ProductOtherImage productOtherImages = new ProductOtherImage();

        productOtherImages.setProductId(rs.getInt("product_id"));
        productOtherImages.setImageUrl(rs.getString("image_url"));

        return productOtherImages;
    }
}
