package com.tmq.module_25.dto;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

import java.util.Date;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AuthResponseDto (
        Long userId,
        String token,
        Date issuedAt,
        Date expiresAt
){
}
