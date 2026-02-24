package com.tmq.individuals_api.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @Qualifier(value = "keycloakWebClient")
    WebClient keycloakWebClient () {
        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }
}
