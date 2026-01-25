package com.ny.archive.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI nyArchiveOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NY-Archive API")
                        .description("NY-Archive API 명세서")
                        .version("v1.0.0"));
    }
}
