package com.tmq.repository;

import com.tmq.model.Label;

import java.util.List;
import java.util.Optional;

public interface LabelRepository extends GenericRepository<Label, Long> {
    Optional<Label> findByName(String name);
}
