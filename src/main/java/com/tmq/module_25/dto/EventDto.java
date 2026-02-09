package com.tmq.module_25.dto;

import com.tmq.module_25.entity.EventStatus;

import java.util.Date;

public class EventDto {
    private Long id;
    private Long userId;
    private Long fileId;
    private EventStatus status;
    private Date timestamp;
}
