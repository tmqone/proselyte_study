package com.tmq.personapi.repository;

import com.tmq.personapi.model.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<AddressEntity, UUID> {
}
