package com.tmq.module_25.repository;

import com.tmq.module_25.entity.EventEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface EventRepository extends R2dbcRepository<EventEntity, Long> {
}
