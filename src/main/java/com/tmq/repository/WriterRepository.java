package com.tmq.repository;

import com.tmq.model.Writer;

import java.util.Optional;

public interface WriterRepository extends GenericRepository<Writer, Long> {
    Optional<Writer> findByName(String name);
}
