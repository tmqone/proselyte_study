package com.tmq.dto.event;

import com.tmq.model.Action;

public record CreateEventRequest(Integer userId, Integer fileId, Action action) {
}
