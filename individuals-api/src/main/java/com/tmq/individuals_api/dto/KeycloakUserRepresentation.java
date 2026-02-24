package com.tmq.individuals_api.dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record KeycloakUserRepresentation (
        @Email(message = "Email is not valid")
        String email,
        Map<String, String> attributes,
        boolean enabled,
        boolean emailVerified,
        List<String> requiredActions,
        List<KeycloakCredentialRepresentation> credentials
){
}
