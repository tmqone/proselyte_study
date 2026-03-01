package com.tmq.individuals_api.dto;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record KeycloakUserRepresentation (
        String email,
        Map<String, String> attributes,
        boolean enabled,
        boolean emailVerified,
        List<String> requiredActions,
        List<KeycloakCredentialRepresentation> credentials
){
}
