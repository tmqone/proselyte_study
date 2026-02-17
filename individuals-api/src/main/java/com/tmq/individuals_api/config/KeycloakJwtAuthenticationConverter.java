package com.tmq.individuals_api.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Flux;

import java.util.List;

public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, Flux<GrantedAuthority>> {
    @Override
    public Flux<GrantedAuthority> convert(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("roles");
        if (roles == null || roles.isEmpty()) {
            return Flux.empty();
        }
        return Flux.fromIterable(roles)
                .map(SimpleGrantedAuthority::new);
    }
}
