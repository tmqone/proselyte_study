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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LabelControllerImpl implements LabelController {
    private static final LabelControllerImpl INSTANCE = new LabelControllerImpl();
    private static final LabelRepository REPOSITORY = GsonLabelRepositoryImpl.getInstance();
    private final InputValidator inputValidator = new InputValidator();
    private static final String NOT_FOUND_MESSAGE = "Label not found";

    public static LabelControllerImpl getInstance() {
        return INSTANCE;
    }

    public List<Label> getAll() {
        return REPOSITORY.findAll();
    }

    public Optional<Label> getByName(String name) {
        name = name.trim();
        try {
            if (!inputValidator.validate(name)) {
                return Optional.empty();
            }
            return Optional.of(REPOSITORY.findByName(name))
                    .orElseThrow(() -> new ObjectNotFoundException(NOT_FOUND_MESSAGE));
        } catch (ObjectNotFoundException e) {
            return Optional.empty();
        }
    }

    public Optional<Label> getById(String id) {
        id = id.trim();
        try {
            if (!inputValidator.validateLongString(id)) {
                return Optional.empty();
            }
            return Optional.of(REPOSITORY.findById(Long.parseLong(id)))
                    .orElseThrow(() -> new ObjectNotFoundException(NOT_FOUND_MESSAGE));
        } catch (ObjectNotFoundException e) {
            return Optional.empty();
        }
    }

    public boolean save(String name) {
        name = name.trim();
        try {
            if (!inputValidator.validate(name)) {
                return false;
            }

            return REPOSITORY.save(Label.builder().name(name).status(Status.ACTIVE).build());
        } catch (ObjectExistsException e) {
            return false;
        } catch (IOException e) {
            throw new GenericExceptionHandler(e.getMessage());
        }
    }

    @Override
    public boolean update(String name, String id) {
        name = name.trim();
        id = id.trim();
        try {
            if (!inputValidator.validate(name) || !inputValidator.validateLongString(id)) {
                return false;
            }
            return REPOSITORY.update(Label.builder().name(name).id(Long.parseLong(id)).status(Status.ACTIVE).build());

        } catch (ObjectExistsException | ObjectNotFoundException | NumberFormatException e) {
            return false;
        } catch (IOException e) {
            throw new GenericExceptionHandler(e.getMessage());
        }
    }

    @Override
    public boolean delete(String id) {
        id = id.trim();
        try {
            if (!inputValidator.validateLongString(id)) {
                return false;
            }
            Label label = REPOSITORY.findById(Long.parseLong(id))
                    .orElseThrow(() -> new ObjectNotFoundException(NOT_FOUND_MESSAGE));
            label.setStatus(Status.DELETED);
            return REPOSITORY.delete(label);
        } catch (ObjectNotFoundException | NumberFormatException e) {
            return false;
        } catch (IOException e) {
            throw new GenericExceptionHandler(e.getMessage());
        }
    }

    public List<Label> getAndSaveLabels(String labels) {
        String[] labelsTrimmed = labels.split(",\\s");
        List<String> labelsToSave = new ArrayList<>();

        for (String s : labelsTrimmed) {
            if (REPOSITORY.findByName(s).isEmpty()) {
                labelsToSave.add(s);
            }
        }

        labelsToSave.forEach(label -> {
            try {
                REPOSITORY.save(Label.builder()
                        .name(label)
                        .status(Status.ACTIVE)
                        .id(findLastId())
                        .build());
            } catch (IOException e) {
                throw new GenericExceptionHandler(e.getMessage());
            }
        });

        return IntStream.range(0, labelsTrimmed.length)
                .mapToObj(x -> Label.builder()
                        .name(labelsTrimmed[x])
                        .id(getByName(labelsTrimmed[x])
                                .orElseThrow(() -> new ObjectNotFoundException(NOT_FOUND_MESSAGE)).getId())
                        .status(Status.ACTIVE).build())
                .toList();
    }

    public Long findLastId() {
        try {
            return REPOSITORY.findAll().stream()
                    .max(Comparator.comparing(Label::getId))
                    .orElseThrow(() -> new ObjectNotFoundException(NOT_FOUND_MESSAGE)).getId();
        } catch (ObjectNotFoundException e) {
            return 1L;
        }
    }
}
