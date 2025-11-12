package com.tmq.dto.event;

import com.tmq.model.Action;

public record UpdateEventRequest(Integer id, Integer userId, Integer fileId, Action action) {
}
