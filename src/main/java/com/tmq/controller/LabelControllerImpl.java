package com.tmq.controller;

import com.tmq.exception.*;
import com.tmq.model.Label;
import com.tmq.model.Status;
import com.tmq.repository.GsonLabelRepositoryImpl;
import com.tmq.repository.LabelRepository;
import com.tmq.validator.InputValidator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LabelControllerImpl implements LabelController {
    private static final LabelControllerImpl INSTANCE = new LabelControllerImpl();
    private static final LabelRepository REPOSITORY = GsonLabelRepositoryImpl.getInstance();
    private final InputValidator inputValidator = new InputValidator();

    public static LabelControllerImpl getInstance() {
        return INSTANCE;
    }

    public List<Label> getAll() {
        return REPOSITORY.findAll();
    }

    public Label getByName(String name) {
        name = name.trim();
        if (!inputValidator.validate(name)) {
            throw new NotCorrectInputException();
        }
        return REPOSITORY.findByName(name)
                .orElseThrow(LabelNotFoundException::new);

    }

    public Label getById(String id) {
        id = id.trim();
        if (!inputValidator.validateLongString(id)) throw new NotCorrectInputException();

        return REPOSITORY.findById(Long.parseLong(id))
                .orElseThrow(LabelNotFoundException::new);
    }

    public boolean save(String name) {
        name = name.trim();
        if (!inputValidator.validate(name)) throw new NotCorrectInputException();
        return REPOSITORY.save(Label.builder().name(name).status(Status.ACTIVE).build());
    }

    @Override
    public boolean update(String name, String id) {
        name = name.trim();
        id = id.trim();
        if (!inputValidator.validate(name) || !inputValidator.validateLongString(id)) {
            throw new NotCorrectInputException();
        }
        return REPOSITORY.update(Label.builder().name(name).id(Long.parseLong(id)).status(Status.ACTIVE).build());
    }

    @Override
    public boolean delete(String id) {
        id = id.trim();
        if (!inputValidator.validateLongString(id)) {
            throw new NotCorrectInputException();
        }
        return REPOSITORY.delete(Label.builder().id(Long.parseLong(id)).status(Status.DELETED).build());
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

    public List<Label> getActiveLabels(List<Label> labelList) {
        try {
            Iterator<Label> iterator = labelList.iterator();
            while (iterator.hasNext()) {
                Label next = iterator.next();
                REPOSITORY
                        .findById(next.getId()).ifPresentOrElse(label -> {
                        }, iterator::remove);
            }
            return labelList;
        } catch (LabelNotFoundException e) {
            return Collections.emptyList();
        }
    }
}
