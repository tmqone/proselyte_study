package com.tmq.module_25.repository;

import com.tmq.module_25.entity.FileEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface FileRepository extends R2dbcRepository<FileEntity, Long> {
    @Query("""
        SELECT f.*
        FROM files f
        JOIN events e ON e.file_id = f.id
        WHERE e.user_id = :userId and e.status = 'CREATED' and f.id = :fileId
    """)
    Mono<FileEntity> findAllByUserId(Long userId, Long fileId);
}
