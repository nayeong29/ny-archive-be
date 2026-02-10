package com.ny.archive.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 브라우저에서 /images/** 로 시작하는 주소로 요청이 온다
        registry.addResourceHandler("/images/**")
                // 내 로컬 폴더 안에서 파일을 찾아라
                .addResourceLocations("file:/Users/nellie/backend-dev/images/");
    }
}