package com.tmq.controller;

import com.tmq.exception.GenericExceptionHandler;
import com.tmq.exception.ObjectExistsException;
import com.tmq.exception.ObjectNotFoundException;
import com.tmq.model.Label;
import com.tmq.model.Status;
import com.tmq.repository.GsonLabelRepositoryImpl;
import com.tmq.repository.LabelRepository;
import com.tmq.validator.InputValidator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LabelControllerImpl implements LabelController {
    private static final LabelControllerImpl INSTANCE = new LabelControllerImpl();
    private static final LabelRepository REPOSITORY = GsonLabelRepositoryImpl.getInstance();
    private final InputValidator inputValidator = new InputValidator();
    private static final String NOT_FOUND_MESSAGE = "Тэг не найден";
    private static final String NOT_CORRECT_INPUT = "Некорретный ввод";

    public static LabelControllerImpl getInstance() {
        return INSTANCE;
    }

    public List<Label> getAll() {
        return REPOSITORY.findAll();
    }

    public Label getByName(String name) {
        name = name.trim();
        if (!inputValidator.validate(name)) {
            throw new GenericExceptionHandler(NOT_CORRECT_INPUT);
        }
        return REPOSITORY.findByName(name)
                .orElseThrow(() -> new ObjectNotFoundException(NOT_FOUND_MESSAGE));

    }

    public Label getById(String id) {
        id = id.trim();
        if (!inputValidator.validateLongString(id)) throw new GenericExceptionHandler(NOT_CORRECT_INPUT);

        return REPOSITORY.findById(Long.parseLong(id))
                .orElseThrow(() -> new ObjectNotFoundException(NOT_FOUND_MESSAGE));
    }

    public boolean save(String name) {
        name = name.trim();
        try {
            if (!inputValidator.validate(name)) throw new GenericExceptionHandler(NOT_CORRECT_INPUT);

            return REPOSITORY.save(Label.builder().name(name).status(Status.ACTIVE).build());
        } catch (ObjectExistsException e) {
            throw new GenericExceptionHandler(NOT_CORRECT_INPUT);
        }
    }

    @Override
    public boolean update(String name, String id) {
        name = name.trim();
        id = id.trim();
        try {
            if (!inputValidator.validate(name) || !inputValidator.validateLongString(id)) {
                throw new GenericExceptionHandler(NOT_CORRECT_INPUT);
            }
            return REPOSITORY.update(Label.builder().name(name).id(Long.parseLong(id)).status(Status.ACTIVE).build());

        } catch (ObjectExistsException | ObjectNotFoundException e) {
            throw new GenericExceptionHandler(e.getMessage());
        }
    }

    @Override
    public boolean delete(String id) {
        id = id.trim();
        try {
            if (!inputValidator.validateLongString(id)) {
                throw new GenericExceptionHandler(NOT_CORRECT_INPUT);
            }
            return REPOSITORY.delete(Label.builder().id(Long.parseLong(id)).status(Status.DELETED).build());
        } catch (ObjectNotFoundException e) {
            throw new GenericExceptionHandler(NOT_FOUND_MESSAGE);
        }
    }

    public List<Label> getAndSaveLabels(String labels) {
        String[] labelsTrimmed = labels.split("\\s*,\\s*");
        List<String> labelsToSave = new ArrayList<>();

        for (String s : labelsTrimmed) {
            if (REPOSITORY.findByName(s).isEmpty()) {
                labelsToSave.add(s);
            }
        }

        labelsToSave.forEach(label -> {
            REPOSITORY.save(Label.builder()
                    .name(label)
                    .status(Status.ACTIVE)
                    .build());
        });

        return IntStream.range(0, labelsTrimmed.length)
                .mapToObj(x -> Label.builder()
                        .status(Status.ACTIVE)
                        .id(getByName(labelsTrimmed[x]).getId())
                        .name(labelsTrimmed[x]).build())
                .toList();
    }
}
