package com.tmq.personapi.repository;

import com.tmq.personapi.model.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CountryRepository extends JpaRepository<CountryEntity, Integer> {
}
