package com.stuby.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean("webClientWithFilter")
    WebClient webClientWithFilter(WebClient.Builder webClientBuilder, ExchangeFilterFunction interceptionFilter) {
        return webClientBuilder.filter(interceptionFilter).build();
    }
}
