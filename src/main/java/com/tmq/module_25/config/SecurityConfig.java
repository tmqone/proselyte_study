package com.tmq.module_25.config;

import com.tmq.module_25.security.AuthenticationManager;
import com.tmq.module_25.security.BearerTokenServerAuthenticationConverter;
import com.tmq.module_25.security.JwtHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import reactor.core.publisher.Mono;

@EnableReactiveMethodSecurity
@Configuration
@Slf4j
public class SecurityConfig {

    @Value("${spring.jwt.secret}")
    private String secret;

    private final String[] publicRoutes = {
            "api/v1/auth/register",
            "api/v1/auth/login"
    };

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, AuthenticationManager authenticationManager) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorizeExchangeSpec -> {
                    authorizeExchangeSpec.pathMatchers(HttpMethod.OPTIONS).permitAll();
                    authorizeExchangeSpec.pathMatchers(publicRoutes).permitAll();
                    authorizeExchangeSpec.anyExchange().authenticated();
                })
                .addFilterAt(bearerAuthenticationFilter(authenticationManager), SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(exceptionHandlingSpec ->
                        exceptionHandlingSpec
                                .authenticationEntryPoint((swe, e) -> {
                                    return Mono.fromRunnable(() -> {
                                        swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                                    });
                                })
                                .accessDeniedHandler((exchange, denied) -> {
                                    return Mono.fromRunnable(() -> {
                                        exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                                    });
                                }))
                .build();

    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    private AuthenticationWebFilter bearerAuthenticationFilter(AuthenticationManager authenticationManager) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(new BearerTokenServerAuthenticationConverter(new JwtHandler(secret)));
        authenticationWebFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/**"));
        return authenticationWebFilter;
    }
}
