package com.tmq.individuals.service;

import com.tmq.individuals.dto.TokenResponse;
import com.tmq.individuals.client.KeycloakClient;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.observability.micrometer.Micrometer;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final KeycloakClient keycloakClient;
    private final ObservationRegistry observationRegistry;

    public Mono<TokenResponse> getAccessToken(String email, String password){
        return keycloakClient.getUserToken(email, password)
                .doOnNext(tokenResponse -> log.info("For user {} was generated access token", email))
                .name("token.getAccessToken")
                .tap(Micrometer.observation(observationRegistry));
    }

    public Mono<TokenResponse> refreshToken(String refreshToken){
        return keycloakClient.refreshToken(refreshToken)
                .name("token.getRefreshToken")
                .tap(Micrometer.observation(observationRegistry));
    }

    public Mono<TokenResponse> getAdminToken(){
        return keycloakClient.getAdminToken()
                .name("token.getAdminToken")
                .tap(Micrometer.observation(observationRegistry));
    }
}
