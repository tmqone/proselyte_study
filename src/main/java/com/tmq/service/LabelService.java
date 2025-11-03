package com.tmq.service;

import com.tmq.model.Label;

import java.util.List;

public interface LabelService extends GenericService<Label> {
    List<Label> getAll();

    Label getByName(String name);

    List<Label> getByName(List<Label> labels);

    Label getById(Long id);

    Label save(Label label);

    List<Label> save(List<Label> labels);

    Label update(Label label);

    boolean delete(Long id);

    List<Label> findOrCreateLabels(List<Label> labels);
}
