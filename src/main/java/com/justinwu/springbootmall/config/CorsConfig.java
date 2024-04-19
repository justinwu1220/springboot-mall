package com.justinwu.springbootmall.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        WebMvcConfigurer.super.addCorsMappings(registry);
        registry.addMapping("/**")//配置可跨域路徑
                .allowedOrigins("*")//允許所有請求域名訪問
                .allowedHeaders("*")//允許所有請求Header訪問
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")//允許請求方法訪問
                .maxAge(3600);//配置客戶端可以緩存pre-fight請求的響應時間(秒)
    }
}
