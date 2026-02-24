package com.tmq.individuals_api.dto;

import lombok.Builder;

@Builder
public record KeycloakCredentialRepresentation (
        String type,
        boolean temporary,
        String value
){
}
