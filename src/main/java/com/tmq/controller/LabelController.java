package com.tmq.controller;

import com.tmq.dto.LabelDto;
import com.tmq.model.Label;
import java.util.List;

public interface LabelController extends GenericController<LabelDto> {
    List<LabelDto> getAll();

    LabelDto getByName(String name);

    LabelDto getById(String id);

    LabelDto save(String name);

    LabelDto update(String name, String id);

    boolean delete(String id);
}
