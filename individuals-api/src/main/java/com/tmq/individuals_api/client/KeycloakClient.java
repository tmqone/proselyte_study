package com.tmq.individuals_api.client;

import com.tmq.common.dto.TokenResponse;
import com.tmq.individuals_api.dto.KeycloakUserRepresentation;
import com.tmq.individuals_api.exception.KeycloakException;
import com.tmq.individuals_api.metrics.KeycloakMetrics;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.observability.micrometer.Micrometer;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

import static com.tmq.individuals_api.metrics.KeycloakMetrics.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class KeycloakClient {
    private final WebClient keycloakWebClient;
    private final KeycloakMetrics keycloakMetrics;
    private final ObservationRegistry observationRegistry;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    private static final String BEARER_PREFIX = "Bearer ";
    private static final int TOKEN_EXPIRY_BUFFER_SECONDS = 30;

    private record CachedToken(TokenResponse token, Instant expiresAt) {
        boolean isValid() {
            return Instant.now().isBefore(expiresAt);
        }
    }

    private final AtomicReference<CachedToken> adminTokenCache = new AtomicReference<>();

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
                .doOnError(ignored -> keycloakMetrics.recordError(OP_GET_USER_TOKEN))
                .name("keycloak.getUserToken")
                .tap(Micrometer.observation(observationRegistry));
    }

    public Mono<TokenResponse> getAdminToken() {
        return Mono.defer(() -> {
            CachedToken cached = adminTokenCache.get();
            if (cached != null && cached.isValid()) {
                log.info("Returning cached admin token, expires at {}", cached.expiresAt());
                return Mono.just(cached.token());
            }
            return fetchAdminToken();
        });
    }

    private Mono<TokenResponse> fetchAdminToken() {
        return keycloakWebClient.post()
                .uri("/realms/payment-system/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "client_credentials")
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret))
                .retrieve()
                .bodyToMono(TokenResponse.class)
                .doOnNext(token -> {
                    if (token.getExpiresIn() == null) {
                        throw new KeycloakException("Admin token response missing expires_in", "KEYCLOAK_EXCEPTION");
                    }
                    Instant expiresAt = Instant.now().plusSeconds(token.getExpiresIn() - TOKEN_EXPIRY_BUFFER_SECONDS);
                    adminTokenCache.set(new CachedToken(token, expiresAt));
                    log.info("Admin token fetched and cached until {}", expiresAt);
                })
                .doOnNext(ignored -> keycloakMetrics.recordSuccess(OP_GET_ADMIN_TOKEN))
                .doOnError(ignored -> keycloakMetrics.recordError(OP_GET_ADMIN_TOKEN))
                .name("keycloak.getAdminToken")
                .tap(Micrometer.observation(observationRegistry));
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
                .doOnError(ignored -> keycloakMetrics.recordError(OP_REFRESH_TOKEN))
                .name("keycloak.refreshToken")
                .tap(Micrometer.observation(observationRegistry));
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
                .doOnError(ignored -> keycloakMetrics.recordError(OP_CREATE_USER))
                .name("keycloak.createUser")
                .tap(Micrometer.observation(observationRegistry));
    }
}