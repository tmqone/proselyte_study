package com.tmq.individuals_api.client;

import com.tmq.individuals_api.dto.TokenResponse;
import com.tmq.individuals_api.dto.UserInfoResponse;
import com.tmq.individuals_api.dto.UserLoginRequest;
import com.tmq.individuals_api.dto.UserRegistrationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class KeycloakClient {
    private final WebClient keycloakWebClient;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    private Mono<TokenResponse> getUserToken(String username, String password) {
        return keycloakWebClient.post()
                .uri("/realms/payment-system/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "password")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("username", username)
                        .with("password", password))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .doOnNext(body -> log.error("Keycloak token error {}: {}", response.statusCode(), body))
                                .flatMap(body -> Mono.error(new RuntimeException("Keycloak token error: " + body))))
                .bodyToMono(TokenResponse.class);
    }

    private Mono<TokenResponse> getAccessToken () {
        return keycloakWebClient.post()
                .uri("/realms/payment-system/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "client_credentials")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret))
                .retrieve()
                .bodyToMono(TokenResponse.class);
    }

    public Mono<TokenResponse> createNewUser(String username, String password) {
        Mono<TokenResponse> accessToken = getAccessToken();

        Map<String, Object> userBody = Map.of(
                "username", username,
                "enabled", true,
                "emailVerified", true,
                "requiredActions", List.of(),
                "credentials", List.of(Map.of(
                        "type", "password",
                        "temporary", false,
                        "value", password
                ))
        );


        return accessToken.flatMap(tokenResponse -> keycloakWebClient
                .post()
                .uri("/admin/realms/payment-system/users")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userBody)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .doOnNext(body -> log.error("Keycloak error {}: {}", response.statusCode(), body))
                                .flatMap(body -> Mono.error(new RuntimeException("Keycloak error: " + body))))
                .toBodilessEntity()
                .flatMap(ignored -> getUserToken(request.getEmail(), request.getPassword())));
    }

    public Mono<UserInfoResponse> getUserInfo(String clientId) {

    }

}
