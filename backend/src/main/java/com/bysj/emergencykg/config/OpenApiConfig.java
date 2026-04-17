package com.bysj.emergencykg.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info().title("基于大语言模型的应急预案知识图谱构建 API").description("面向毕设答辩演示的后端接口文档").version("1.0.0").license(new License().name("Graduation Design Demo")));
    }
}
