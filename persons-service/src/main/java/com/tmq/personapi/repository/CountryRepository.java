package com.tmq.personapi.repository;

import com.tmq.personapi.model.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<CountryEntity, Integer> {
    Optional<CountryEntity> findByName(String name);
}
