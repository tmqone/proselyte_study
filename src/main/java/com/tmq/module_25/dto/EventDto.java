package com.tmq.module_25.dto;

import com.tmq.module_25.entity.EventStatus;

import java.util.Date;

public class EventDto {
    private Long id;
    private UserDto user;
    private FileDto file;
    private EventStatus status;
    private Date timestamp;
}
