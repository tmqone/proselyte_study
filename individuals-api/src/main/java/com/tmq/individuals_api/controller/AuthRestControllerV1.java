package com.tmq.individuals_api.controller;

import com.tmq.common.dto.*;
import com.tmq.common.api.individuals_api.*;
import com.tmq.individuals_api.service.TokenService;
import com.tmq.individuals_api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthRestControllerV1 implements AuthApi {
    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("/registration")
    @Override
    public Mono<ResponseEntity<TokenResponse>> authRegistrationPost(Mono<UserRegistrationRequest> userRegistrationRequest, ServerWebExchange exchange) {
        return userRegistrationRequest
                .flatMap(userService::register)
                .map(token -> ResponseEntity.status(HttpStatus.CREATED).body(token));
    }

    @PostMapping("/login")
    @Override
    public Mono<ResponseEntity<TokenResponse>> authLoginPost(Mono<UserLoginRequest> userLoginRequest, ServerWebExchange exchange) {
        return userLoginRequest
                .flatMap(userService::login)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/refresh-token")
    @Override
    public Mono<ResponseEntity<TokenResponse>> authRefreshTokenPost(Mono<TokenRefreshRequest> tokenRefreshRequest, ServerWebExchange exchange) {
        return tokenRefreshRequest
                .flatMap(req -> tokenService.refreshToken(req.getRefreshToken()))
                .map(ResponseEntity::ok);
    }

    @GetMapping("/me")
    @Override
    public Mono<ResponseEntity<UserInfoResponse>> authMeGet(ServerWebExchange exchange) {
        return exchange.getPrincipal()
                .cast(Authentication.class)
                .flatMap(userService::getUserInfo)
                .map(ResponseEntity::ok);
    }
}
