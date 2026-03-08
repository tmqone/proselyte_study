package com.tmq.individuals.dto;

import lombok.Builder;

@Builder
public record KeycloakCredentialRepresentation (
        String type,
        boolean temporary,
        String value
){
        @Override
        public String toString() {
                return "KeycloakCredentialRepresentation{" +
                        "type='" + type + '\'' +
                        ", temporary=" + temporary +
                        ", value='*password is hidden*" + '\'' +
                        '}';
        }
}
