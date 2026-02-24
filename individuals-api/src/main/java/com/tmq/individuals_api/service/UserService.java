package com.tmq.individuals_api.service;

import com.tmq.individuals_api.client.KeycloakClient;
import com.tmq.individuals_api.dto.*;
import com.tmq.individuals_api.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final KeycloakClient keycloakClient;
    private final TokenService tokenService;

    public Mono<TokenResponse> register(UserRegistrationRequest request) {

        return UserValidator.validateOnRegistration(
                        request.getEmail(),
                        request.getPassword(),
                        request.getConfirmPassword()
                )
                .then(Mono.defer(() -> {
                    KeycloakUserRepresentation user = KeycloakUserRepresentation.builder()
                            .email(request.getEmail())
                            .emailVerified(true)
                            .enabled(true)
                            .requiredActions(List.of())
                            .attributes(Map.of("created_at", LocalDateTime.now().toString()))
                            .credentials(
                                    List.of(KeycloakCredentialRepresentation.builder()
                                            .type("password")
                                            .value(request.getPassword())
                                            .temporary(false)
                                            .build())
                            ).build();

                    return tokenService.getAdminToken()
                            .flatMap(token ->
                                    keycloakClient.createNewUser(user, token.getAccessToken())
                                            .then(tokenService.getAccessToken(request.getEmail(), request.getPassword()))
                            );
                }));
    }

    public Mono<TokenResponse> login(UserLoginRequest request) {
        return tokenService.getAccessToken(request.getEmail(), request.getPassword());
    }

    public Mono<UserInfoResponse> getUserInfo(Authentication authentication){
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return Mono.just(new UserInfoResponse())
                    .flatMap(userInfoResponse -> {
                        userInfoResponse.setEmail(jwt.getClaimAsString("email"));
                        userInfoResponse.setId(jwt.getSubject());
                        userInfoResponse.setRoles(jwt.getClaimAsStringList("roles"));
                        userInfoResponse.setCreatedAt(OffsetDateTime
                                .of(LocalDateTime.parse(jwt.getClaim("created_at")), ZoneOffset.UTC));
                        return Mono.just(userInfoResponse);
                    });
        } else {
            return Mono.error(new RuntimeException("Invalid user principal"));
        }
    }
}
