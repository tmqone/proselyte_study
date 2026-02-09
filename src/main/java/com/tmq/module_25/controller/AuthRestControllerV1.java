package com.tmq.module_25.controller;

import com.tmq.module_25.dto.AuthRequestDto;
import com.tmq.module_25.dto.AuthResponseDto;
import com.tmq.module_25.dto.UserDto;
import com.tmq.module_25.entity.UserEntity;
import com.tmq.module_25.mapper.UserMapper;
import com.tmq.module_25.security.CustomPrincipal;
import com.tmq.module_25.security.SecurityService;
import com.tmq.module_25.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthRestControllerV1 {
    private final SecurityService securityService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public Mono<UserDto> register(@RequestBody UserDto dto) {
        UserEntity userEntity = userMapper.map(dto);
        return userService.register(userEntity)
                .map(userMapper::map);
    }

    @PostMapping("/login")
    public Mono<AuthResponseDto> login (@RequestBody AuthRequestDto dto) {
        return securityService.authenticate(dto.username(), dto.password())
                .flatMap(tokenDetails -> Mono.just(new AuthResponseDto(
                        tokenDetails.getUserId(),
                        tokenDetails.getToken(),
                        tokenDetails.getIssuedAt(),
                        tokenDetails.getExpiresAt()
                )));
    }

    @GetMapping("/info")
    public Mono<UserDto> getUserInfo(Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return userService.getUserById(principal.getId())
                .map(userMapper::map);
    }


}
