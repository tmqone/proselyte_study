package com.tmq.individuals_api.service;

import com.tmq.individuals_api.client.KeycloakClient;
import com.tmq.individuals_api.dto.TokenResponse;
import com.tmq.individuals_api.dto.UserLoginRequest;
import com.tmq.individuals_api.dto.UserRegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final KeycloakClient keycloakClient;

    public Mono<TokenResponse> register(UserRegistrationRequest request) {
        return keycloakClient.createNewUser(request);
    }

    public Mono<TokenResponse> login(UserLoginRequest request) {
        return keycloakClient.getUser();
    }
}
