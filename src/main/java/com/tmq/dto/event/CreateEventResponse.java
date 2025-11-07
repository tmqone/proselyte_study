package com.tmq.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmq.dto.entity.FileDto;
import com.tmq.dto.entity.UserDto;
import com.tmq.model.Action;

public record CreateEventResponse(
        Integer id,
        @JsonIgnoreProperties("events")
        UserDto user,
        FileDto file,
        Action action) {
}
