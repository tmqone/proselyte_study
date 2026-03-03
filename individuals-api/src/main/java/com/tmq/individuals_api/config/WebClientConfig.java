package com.tmq.individuals_api.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${keycloak.url}")
    private String keycloakUrl;
    @Value("${keycloak.port}")
    private String keycloakPort;

    @Bean
    @Qualifier(value = "keycloakWebClient")
    WebClient keycloakWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(String.format("%s:%s", keycloakUrl, keycloakPort))
                .build();
    }
}
