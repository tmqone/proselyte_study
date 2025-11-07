package com.tmq.dto.entity;

import com.tmq.model.Action;

public record EventDto (Integer id, UserDto userDto, FileDto fileDto, Action action) {
}
