package com.tmq.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmq.dto.entity.FileDto;
import com.tmq.dto.entity.UserWithoutEventDto;
import com.tmq.model.Action;
import com.tmq.model.File;
import com.tmq.model.User;

public record FindEventByIdResponse(
        Integer id,
        UserWithoutEventDto user,
        FileDto file,
        Action action) {
}
