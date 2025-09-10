package com.tmq.repository;

import com.tmq.model.Writer;

import java.util.List;
import java.util.Optional;

public interface WriterRepository extends GenericRepository<Writer, Long> {
    List<Writer> findByName(String secondName, String firstName);
}
