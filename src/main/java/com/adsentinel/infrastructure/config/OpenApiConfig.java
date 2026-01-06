package com.adsentinel.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI adSentinelOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("AdSentinel API")
                        .description("Enterprise Marketing Automation System for Google Ads Management")
                        .version("v1.0")
                        .license(new License().name("Private Enterprise License").url("https://adsentinel.internal")));
    }
}
