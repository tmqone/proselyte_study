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
public class AuthRestControllerV1 {
    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("/registration")
    public Mono<ResponseEntity<TokenResponse>> registration (@RequestBody Mono<UserRegistrationRequest> request) {
        return request.flatMap(userService::register)
                .map(tokenResponse -> ResponseEntity.status(HttpStatus.CREATED).body(tokenResponse));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<TokenResponse>> login (@RequestBody Mono<UserLoginRequest> request) {
        return request.flatMap(userService::login)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/refresh-token")
    public Mono<ResponseEntity<TokenResponse>> refreshToken(@RequestBody Mono<TokenRefreshRequest> request) {
        return request
                .flatMap(tokenRequest -> tokenService.refreshToken(tokenRequest.getRefreshToken()))
                .map(ResponseEntity::ok);
    }

    @GetMapping("/me")
    public Mono<UserInfoResponse> getUserInfo(Authentication authentication){
        return userService.getUserInfo(authentication);
    }
}
