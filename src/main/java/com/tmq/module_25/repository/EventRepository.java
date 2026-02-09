package com.tmq.module_25.repository;

import com.tmq.module_25.entity.EventEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface EventRepository extends R2dbcRepository<EventEntity, Long> {
    @Query("""
            update events e
            set status = DELETED
            where e.id = :id
            """)
    Mono<Void> deleteEvent(Long id);
}
