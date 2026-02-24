package com.tmq.individuals_api.controller;

import com.tmq.individuals_api.dto.*;
import com.tmq.individuals_api.service.TokenService;
import com.tmq.individuals_api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthControllerV1 {
    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("/registration")
    public ResponseEntity<Mono<TokenResponse>> registration (@RequestBody UserRegistrationRequest request) {
        return userService.register(request)
                .doOnSuccess(tokenResponse -> {
                    return ResponseEntity.status(HttpStatus.CREATED).body(tokenResponse);
                });
    }

    @PostMapping("/login")
    public ResponseEntity<Mono<TokenResponse>> login (UserLoginRequest request) {
        return userService.login(request);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Mono<TokenResponse>> refreshToken(TokenRefreshRequest request) {
        return Mono.empty();
    }

    @GetMapping("/me")
    public Mono<UserInfoResponse> getUserInfo(Authentication authentication){
        return Mono.empty();
    }
}
