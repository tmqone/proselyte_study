package com.tmq.dto.entity;

import java.util.List;

public record UserDto (Integer id, String username, List<EventDto> events) {
}
