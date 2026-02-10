package com.tmq.module_25.dto;

import com.tmq.module_25.entity.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventDto {
    private Long id;
    private Long userId;
    private Long fileId;
    private EventStatus status;
    private LocalDateTime timestamp;
}
