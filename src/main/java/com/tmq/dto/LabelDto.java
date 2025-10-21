package com.tmq.dto;

import lombok.Builder;

@Builder
public record LabelDto (
        Long id,
        String name
) {}
