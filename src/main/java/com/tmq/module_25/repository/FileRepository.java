package com.tmq.module_25.repository;

import com.tmq.module_25.dto.FileDto;
import com.tmq.module_25.entity.FileEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileRepository extends R2dbcRepository<FileEntity, Long> {
    @Query("""
        SELECT f.*
        FROM files f
        JOIN events e ON e.file_id = f.id
        WHERE 
            e.user_id = :userId and 
            e.status = 'CREATED' and 
            f.id = :fileId and
            f.status = 'ACTIVE'
    """)
    Mono<FileEntity> findFileWithUserId(Long userId, Long fileId);

    @Query("""
           update files f
           set status = 'ARCHIVED'
           where f.id = :id
           """)
    Mono<Void> deleteById(Long id);

    Flux<FileEntity> findAll();

    @Query("""
        SELECT f.*
        FROM files f
        JOIN events e ON e.file_id = f.id
        WHERE 
            e.user_id = :userId and 
            e.status = 'CREATED' and 
            f.status = 'ACTIVE'
    """)
    Flux<FileEntity> findAllByUserId(Long userId);

    Mono<FileEntity> findFileEntityById(Long fileId);
}
