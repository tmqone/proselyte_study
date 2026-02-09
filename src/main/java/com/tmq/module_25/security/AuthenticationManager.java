package com.tmq.module_25.security;

import com.tmq.module_25.entity.UserStatus;
import com.tmq.module_25.exception.UnauthorizedException;
import com.tmq.module_25.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {
    private final UserService userService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        CustomPrincipal customPrincipal = (CustomPrincipal) authentication.getPrincipal();
        return userService.getUserById(customPrincipal.getId())
                .filter(entity -> entity.getStatus() == UserStatus.ACTIVE)
                .switchIfEmpty(Mono.error(new UnauthorizedException("User is disabled")))
                .map(user -> authentication);

    }
}
