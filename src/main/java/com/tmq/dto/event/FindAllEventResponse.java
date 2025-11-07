package com.tmq.dto.event;

import com.tmq.model.Action;
import com.tmq.model.Event;
import com.tmq.model.File;
import com.tmq.model.User;

import java.util.List;

public record FindAllEventResponse(Integer id, User user, File file, Action action) {
}
