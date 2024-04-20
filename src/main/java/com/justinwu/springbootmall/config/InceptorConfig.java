package com.justinwu.springbootmall.config;

import com.justinwu.springbootmall.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InceptorConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor tokenInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**");
                //.excludePathPatterns("/users/login", "/users/register", "/products/**", "/users/{userId}/**");
                //.excludePathPatterns("/**");
    }
}
