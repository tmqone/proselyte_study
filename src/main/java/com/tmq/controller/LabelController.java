package com.tmq.controller;

import com.tmq.model.Label;

import java.util.List;
import java.util.Optional;

public interface LabelController extends GenericController<Label> {
    List<Label> getAll();
    Optional<Label> getByName(String name);
    Optional<Label> getById(String id);
    boolean save(String name);
    boolean update(String name, String id);
    boolean delete(String name);
}
