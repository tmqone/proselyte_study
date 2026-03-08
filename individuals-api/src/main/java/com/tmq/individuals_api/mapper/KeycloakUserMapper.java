package com.tmq.individuals_api.mapper;

import com.tmq.individuals.dto.UserRegistrationRequest;
import com.tmq.individuals_api.dto.KeycloakCredentialRepresentation;
import com.tmq.individuals_api.dto.KeycloakUserRepresentation;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class KeycloakUserMapper {

    public KeycloakUserRepresentation toKeycloakUser(UserRegistrationRequest request, String userId) {
        return KeycloakUserRepresentation.builder()
                .email(request.getEmail())
                .emailVerified(true)
                .enabled(true)
                .requiredActions(List.of())
                .attributes(Map.of(
                        "created_at", LocalDateTime.now().toString(),
                        "user_id", userId.toString()
                ))
                .credentials(List.of(
                        KeycloakCredentialRepresentation.builder()
                                .type("password")
                                .value(request.getPassword())
                                .temporary(false)
                                .build()
                ))
                .build();
    }
}