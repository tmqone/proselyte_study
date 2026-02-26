package com.tmq.individuals_api.client;

import com.tmq.individuals_api.dto.KeycloakUserRepresentation;
import com.tmq.individuals_api.dto.TokenResponse;
import com.tmq.individuals_api.exception.KeycloakException;
import com.tmq.individuals_api.metrics.KeycloakMetrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.tmq.individuals_api.metrics.KeycloakMetrics.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class KeycloakClient {
    private final WebClient keycloakWebClient;
    private final KeycloakMetrics keycloakMetrics;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    private static final String BEARER_PREFIX = "Bearer ";

    public Mono<TokenResponse> getUserToken(String email, String password) {

        return keycloakWebClient.post()
                .uri("/realms/payment-system/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("username", email)
                        .with("password", password))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .doOnNext(body -> log.error("Keycloak token error {}: {}", response.statusCode(), body))
                                .flatMap(body -> Mono.error(new KeycloakException(body, "KEYCLOAK_EXCEPTION"))))
                .bodyToMono(TokenResponse.class)
                .doOnNext(ignored -> keycloakMetrics.recordSuccess(OP_GET_USER_TOKEN))
                .doOnError(ignored -> keycloakMetrics.recordError(OP_GET_USER_TOKEN));
    }

    public Mono<TokenResponse> getAdminToken() {

        return keycloakWebClient.post()
                .uri("/realms/payment-system/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "client_credentials")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .doOnNext(ignored -> keycloakMetrics.recordSuccess(OP_GET_ADMIN_TOKEN))
                .doOnError(ignored -> keycloakMetrics.recordError(OP_GET_ADMIN_TOKEN));
    }

    public Mono<TokenResponse> refreshToken(String refreshToken) {

        return keycloakWebClient.post()
                .uri("/realms/payment-system/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "refresh_token")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("refresh_token", refreshToken))
                .retrieve()
                .onStatus(status -> status.isSameCodeAs(HttpStatus.BAD_REQUEST),
                        response -> response.bodyToMono(String.class)
                                .doOnNext(body -> log.error("Keycloak error {}: {}", response.statusCode(), body))
                                .flatMap(body -> Mono.error(new KeycloakException(body, "KEYCLOAK_EXCEPTION"))))
                .bodyToMono(TokenResponse.class)
                .doOnNext(ignored -> keycloakMetrics.recordSuccess(OP_REFRESH_TOKEN))
                .doOnError(ignored -> keycloakMetrics.recordError(OP_REFRESH_TOKEN));
    }

    public Mono<TokenResponse> createNewUser(KeycloakUserRepresentation user, String token) {

        return keycloakWebClient
                .post()
                .uri("/admin/realms/payment-system/users")
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .doOnNext(body -> log.error("Keycloak error {}: {}", response.statusCode(), body))
                                .flatMap(body -> Mono.error(new KeycloakException(body, "KEYCLOAK_EXCEPTION"))))
                .bodyToMono(TokenResponse.class)
                .doOnNext(ignored -> keycloakMetrics.recordSuccess(OP_CREATE_USER))
                .doOnError(ignored -> keycloakMetrics.recordError(OP_CREATE_USER));
    }
}