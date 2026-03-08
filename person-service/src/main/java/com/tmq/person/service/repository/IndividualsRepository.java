package com.tmq.person.service.repository;

import com.tmq.person.service.model.IndividualsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface IndividualsRepository extends JpaRepository<IndividualsEntity, UUID> {
    @Query(""" 
            FROM IndividualsEntity i WHERE (:emails) IS NULL OR i.user.email IN :emails
            """)
    List<IndividualsEntity> findAllByEmails(@Param("emails") List<String> emails);

    @Modifying
    @Query("""
            UPDATE IndividualsEntity i SET i.status = 'BLOCKED' WHERE i.id = :id
            """)
    void softDelete(@Param("id") UUID id);
}
