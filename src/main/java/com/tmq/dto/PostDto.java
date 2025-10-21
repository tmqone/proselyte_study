package com.tmq.dto;

import com.tmq.model.PostStatus;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record PostDto (
        Long id,
        String content,
        Instant created,
        Instant updated,
        WriterDto writer,
        List<LabelDto> labels,
        PostStatus postStatus
){
}
