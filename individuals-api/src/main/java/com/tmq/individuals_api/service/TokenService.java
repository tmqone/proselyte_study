package com.tmq.individuals_api.service;

import com.tmq.individuals_api.client.KeycloakClient;
import com.tmq.individuals_api.dto.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final KeycloakClient keycloakClient;

    public Mono<TokenResponse> getAccessToken(String email, String password){
        return keycloakClient.getUserToken(email, password);
    }

    public Mono<TokenResponse> refreshToken(String refreshToken){
        return keycloakClient.refreshToken(refreshToken);
    }

    public Mono<TokenResponse> getAdminToken(){
        return keycloakClient.getAdminToken();
    }
}
