package com.tmq.dto.event;

import com.tmq.dto.entity.FileDto;
import com.tmq.dto.entity.UserWithoutEventDto;
import com.tmq.model.Action;
import com.tmq.model.Event;
import com.tmq.model.File;
import com.tmq.model.User;

import java.util.List;

public record FindAllEventResponse(Integer id, UserWithoutEventDto user, FileDto file, Action action) {
}
