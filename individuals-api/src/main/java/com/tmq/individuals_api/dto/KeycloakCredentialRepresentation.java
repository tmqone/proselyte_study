package com.tmq.individuals_api.dto;

import lombok.Builder;
import lombok.ToString;

@Builder
public record KeycloakCredentialRepresentation (
        String type,
        boolean temporary,
        @ToString.Exclude
        String value
){
}
