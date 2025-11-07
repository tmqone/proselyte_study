package com.tmq.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tmq.model.Action;
import com.tmq.model.File;
import com.tmq.model.User;

public record FindEventByIdResponse(
        Integer id,
        @JsonIgnoreProperties("events")
        User user,
        File file,
        Action action) {
}
