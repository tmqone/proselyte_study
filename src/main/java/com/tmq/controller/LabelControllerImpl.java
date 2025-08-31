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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LabelControllerImpl implements LabelController {
    private static final LabelControllerImpl INSTANCE = new LabelControllerImpl();
    private static final LabelRepository REPOSITORY = GsonLabelRepositoryImpl.getInstance();
    private final InputValidator inputValidator = new InputValidator();

    public static LabelControllerImpl getInstance() {
        return INSTANCE;
    }

    public List<Label> getAll() {
        List<Label> labels = REPOSITORY.findAll();
        return labels == null ? Collections.emptyList() : labels;
    }

    public Optional<Label> getByName(String name) {
        try {
            if (!inputValidator.validate(name)) { return Optional.empty();}
            return Optional.of(REPOSITORY.findByName(name));
        } catch (ObjectNotFoundException e){
            return Optional.empty();
        }
    }

    public Optional<Label> getById(String id) {
        try {
            if (!inputValidator.validateLongString(id)) {
                return Optional.empty();
            }
            return Optional.of(REPOSITORY.findById(Long.parseLong(id)));
        } catch (ObjectNotFoundException e){
            return Optional.empty();
        }
    }

    public boolean save(String name) {
        try {
            if (!inputValidator.validate(name)) {
                return false;
            }

            return REPOSITORY.save(Label.builder().name(name).status(Status.ACTIVE).build());
        } catch (ObjectExistsException e){
            return false;
        } catch (IOException e) {
            throw new GenericExceptionHandler(e.getMessage());
        }
    }

    @Override
    public boolean update(String name, String id) {
        try {
            if (!inputValidator.validate(name) || !inputValidator.validateLongString(id)) {
                return false;
            }
            return REPOSITORY.update(Label.builder().name(name).id(Long.parseLong(id)).status(Status.ACTIVE).build());

        } catch (ObjectExistsException | ObjectNotFoundException | NumberFormatException e){
            return false;
        } catch (IOException e) {
            throw new GenericExceptionHandler(e.getMessage());
        }
    }

    @Override
    public boolean delete(String id) {
        try {
            if (!inputValidator.validateLongString(id)){
                return false;
            }
            Label label = REPOSITORY.findById(Long.parseLong(id));
            label.setStatus(Status.DELETED);
            return REPOSITORY.delete(label);
        } catch (ObjectNotFoundException | NumberFormatException e){
            return false;
        } catch (IOException e) {
            throw new GenericExceptionHandler(e.getMessage());
        }
    }
}
