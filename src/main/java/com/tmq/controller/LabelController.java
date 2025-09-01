package com.tmq.controller;

import com.tmq.model.Label;

import java.util.List;

public interface LabelController extends GenericController<Label> {
    List<Label> getAll();
    Label getByName(String name);
    Label getById(String id);
    boolean save(String name);
    boolean update(String name, String id);
    boolean delete(String id);
}
