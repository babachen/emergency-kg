package com.bysj.emergencykg.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, AppProperties appProperties) {
        int timeout = appProperties.getAi().getTimeoutMs();
        return builder.setConnectTimeout(Duration.ofMillis(timeout)).setReadTimeout(Duration.ofMillis(timeout)).build();
    }
}
