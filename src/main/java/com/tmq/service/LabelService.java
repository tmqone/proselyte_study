package com.tmq.service;

import com.tmq.model.Label;

import java.util.List;

public interface LabelService extends GenericService<Label> {
    List<Label> getAll();

    Label getByName(String name);

    Label getById(Long id);

    Label save(Label label);

    List<Label> save(List<Label> labels);

    boolean saveLabelToPost(Long labelId, Long postId);

    Label update(Label label);

    boolean delete(Long id);
}
