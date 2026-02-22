package com.tmq.individuals_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {
    private String[] publicRoutes = {
            "/api/v1/auth/registration",
            "api/v1/auth/login",
            "api/v1/auth/refresh-token"
    };

    @Bean
    public SecurityWebFilterChain securityWebFilterChain (ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> {
                    exchange.pathMatchers(publicRoutes).permitAll();
                    exchange.anyExchange().authenticated();
                })
                .oauth2ResourceServer(oauth2 -> {
                    oauth2.jwt(jwtSpec -> {
                        jwtSpec.jwtAuthenticationConverter(jwtAuthenticationConverter());
                    });
                })
                .build();
    }

    private ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
        var converter = new ReactiveJwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakJwtAuthenticationConverter());
        return converter;
    }
}
