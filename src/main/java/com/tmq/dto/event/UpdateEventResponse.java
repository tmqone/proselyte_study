package com.tmq.dto.event;

import com.tmq.dto.entity.FileDto;
import com.tmq.dto.entity.UserDto;
import com.tmq.dto.entity.UserWithoutEventDto;
import com.tmq.model.Action;

public record UpdateEventResponse(Integer id, UserWithoutEventDto user, FileDto file, Action action) {
}
