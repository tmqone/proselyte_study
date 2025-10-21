package com.tmq.service;

import com.tmq.dto.LabelDto;
import com.tmq.model.Label;

import java.util.List;

public interface LabelService extends GenericService<Label> {
    List<Label> getAll();

    Label getByName(String name);

    Label getById(Long id);

    Label save(Label label);

    Label update(Label label);

    boolean delete(Long id);
}
