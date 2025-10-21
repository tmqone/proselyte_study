package com.tmq.controller;

import com.tmq.dto.LabelDto;
import com.tmq.exception.*;
import com.tmq.mapper.LabelMapper;
import com.tmq.model.Label;
import com.tmq.service.LabelService;
import com.tmq.service.LabelServiceImpl;
import com.tmq.util.InputValidator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class LabelControllerImpl implements LabelController {
    private final InputValidator inputValidator;
    private final LabelService labelService;
    private final LabelMapper labelMapper;

    @Override
    public List<LabelDto> getAll() {
        return labelService.getAll().stream()
                .map(labelMapper::fromEntity)
                .toList();
    }

    @Override
    public LabelDto getByName(String name) {
        name = name.trim();
        if (inputValidator.validate(name)) {
            throw new NotCorrectInputException();
        }

        return labelMapper.fromEntity(labelService.getByName(name));
    }

    @Override
    public LabelDto getById(String id) {
        id = id.trim();
        if (inputValidator.validateLongString(id)) throw new NotCorrectInputException();
        return labelMapper.fromEntity(labelService.getById(Long.parseLong(id)));
    }

    @Override
    public LabelDto save(String name) {
        name = name.trim();
        if (inputValidator.validate(name)) throw new NotCorrectInputException();
        return labelMapper.fromEntity(labelService.save(new Label(0L, name)));
    }

    @Override
    public LabelDto update(String name, String id) {

        name = name.trim();
        id = id.trim();
        if (inputValidator.validate(name) || inputValidator.validateLongString(id)) {
            throw new NotCorrectInputException();
        }
        return labelMapper.fromEntity(labelService.update(new Label(Long.parseLong(id), name)));
    }

    @Override
    public boolean delete(String id) {

        id = id.trim();
        if (inputValidator.validateLongString(id)) {
            throw new NotCorrectInputException();
        }

        return labelService.delete(Long.parseLong(id));
    }
}
