package com.tmq.dto.event;

public record UpdateEventRequest(Integer id, Integer userId, Integer fileId, String action) {
}
