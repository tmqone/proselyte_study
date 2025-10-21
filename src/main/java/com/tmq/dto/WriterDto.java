package com.tmq.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record WriterDto (
        Long id,
        String firstName,
        String lastName,
        List<PostDto>posts
){
    @Override
    public String toString() {
        return "%s %s (id=%d)".formatted(firstName, lastName, id);
    }
}
