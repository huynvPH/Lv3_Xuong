package com.example.lv3.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfigurer implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Áp dụng cho tất cả endpoint
                .allowedOrigins(
                        "http://localhost:5173",
                        "http://localhost:3000"
                ) // Cho phép cả cổng Vite (5173) lẫn cấu hình cũ (3000)
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Các phương thức cho phép
                .allowedHeaders("*") // Cho phép tất cả header
                .allowCredentials(true)
                .maxAge(3600); // Thời gian cache (giây)
    }
}