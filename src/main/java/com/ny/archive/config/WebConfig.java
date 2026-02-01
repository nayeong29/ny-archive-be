package com.ny.archive.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000") // 내 프론트엔드 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH") // 허용할 방식
                .allowedHeaders("*"); // 모든 헤더 허용
    }
}