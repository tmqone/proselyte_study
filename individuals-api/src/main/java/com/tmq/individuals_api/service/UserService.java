package com.tmq.individuals_api.service;

import com.tmq.individuals_api.client.KeycloakClient;
import com.tmq.individuals_api.dto.*;
import com.tmq.individuals_api.exception.ApiException;
import com.tmq.individuals_api.metrics.AuthMetrics;
import com.tmq.individuals_api.validator.UserValidator;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final KeycloakClient keycloakClient;
    private final TokenService tokenService;
    private final AuthMetrics authMetrics;

    public Mono<TokenResponse> register(UserRegistrationRequest request) {
        Timer.Sample sample = authMetrics.startRegistrationSample();

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
                            )
                            .doOnNext(ignored -> log.info("New user registered: {}", user));
                }))
                .doFinally(signal -> authMetrics.stopRegistrationTimer(sample))
                .doOnNext(ignored -> authMetrics.recordRegistrationSuccess())
                .doOnError(authMetrics::recordRegistrationError);
    }

    public Mono<TokenResponse> login(UserLoginRequest request) {
        Timer.Sample sample = authMetrics.startLoginSample();

        return tokenService.getAccessToken(request.getEmail(), request.getPassword())
                .doOnNext(ignored -> log.info("User {} just logged in", request.getEmail()))
                .doFinally(signal -> authMetrics.stopLoginTimer(sample))
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
                    .doOnNext(userInfoResponse -> {
                        log.info("User {} request info about himself", userInfoResponse.getEmail());
                        authMetrics.recordUserInfoSuccess();
                    });
        } else {
            authMetrics.recordUserInfoInvalidPrincipal();
            return Mono.error(new ApiException("Invalid user principal", "INVALID_JWT_TOKEN"));
        }
    }
}