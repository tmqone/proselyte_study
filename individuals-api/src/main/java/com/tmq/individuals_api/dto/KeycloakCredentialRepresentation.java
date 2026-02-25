package com.tmq.individuals_api.dto;

import lombok.Builder;
import lombok.ToString;

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
