package com.tmq.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmq.dto.entity.EventDto;

import java.util.List;

public record FindAllUserResponse(Integer id, String username, @JsonIgnoreProperties("userDto") List<EventDto> events) {
}
