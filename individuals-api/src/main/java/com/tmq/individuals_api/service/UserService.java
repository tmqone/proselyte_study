package com.tmq.individuals_api.service;

import com.tmq.individuals_api.client.KeycloakClient;
import com.tmq.individuals_api.dto.*;
import com.tmq.individuals_api.exception.ApiException;
import com.tmq.individuals_api.mapper.KeycloakUserMapper;
import com.tmq.individuals_api.metrics.AuthMetrics;
import com.tmq.individuals_api.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final KeycloakClient keycloakClient;
    private final TokenService tokenService;
    private final AuthMetrics authMetrics;
    private final UserValidator userValidator;
    private final KeycloakUserMapper keycloakUserMapper;

    public Mono<TokenResponse> register(UserRegistrationRequest request) {
        return userValidator.validateOnRegistration(
                        request.getEmail(),
                        request.getPassword(),
                        request.getConfirmPassword()
                )
                .then(Mono.defer(() -> {
                    KeycloakUserRepresentation user = keycloakUserMapper.toKeycloakUser(request);

                    return tokenService.getAdminToken()
                            .flatMap(token ->
                                    keycloakClient.createNewUser(user, token.getAccessToken())
                                            .doOnNext(ignored -> log.info("New user registered: {}", user))
                                            .then(tokenService.getAccessToken(request.getEmail(), request.getPassword()))
                            );

                }))
                .doOnNext(ignored -> authMetrics.recordRegistrationSuccess())
                .doOnError(authMetrics::recordRegistrationError);
    }

    public Mono<TokenResponse> login(UserLoginRequest request) {
        return tokenService.getAccessToken(request.getEmail(), request.getPassword())
                .doOnNext(ignored -> log.info("User {} just logged in", request.getEmail()))
                .doOnNext(ignored -> authMetrics.recordLoginSuccess())
                .doOnError(ignored -> authMetrics.recordLoginError());
    }

    public Mono<UserInfoResponse> getUserInfo(Authentication authentication) {
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return Mono.just(new UserInfoResponse())
                    .flatMap(userInfoResponse -> {
                        userInfoResponse.setEmail(jwt.getClaimAsString("email"));
                        userInfoResponse.setId(jwt.getSubject());
                        userInfoResponse.setRoles(jwt.getClaimAsStringList("roles"));
                        userInfoResponse.setCreatedAt(OffsetDateTime
                                .of(LocalDateTime.parse(jwt.getClaim("created_at")), ZoneOffset.UTC));
                        return Mono.just(userInfoResponse);
                    })
                    .doOnNext(userInfoResponse -> log.info("User {} request info about himself", userInfoResponse.getEmail()));
        } else {
            return Mono.error(new ApiException("Invalid user principal", "INVALID_JWT_TOKEN"));
        }
    }
}