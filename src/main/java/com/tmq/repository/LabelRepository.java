package com.tmq.repository;

import com.tmq.model.Label;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface LabelRepository extends GenericRepository<Long, Label>{
    Optional<Label> findByName(String name);
    List<Label> findByName(List<Label> labels);

    List<Label> findOrCreateLabels(List<Label> labels);
}
