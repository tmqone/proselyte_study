package com.tmq.module_25.repository;

import com.tmq.module_25.entity.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends R2dbcRepository<UserEntity, Long> {
    Mono<UserEntity> findByUsername(String username);

    @Query("""
           update users u
           set status = 'BLOCKED'
           where u.id = :id
           """)
    Mono<Void> deleteById(Long id);
}
