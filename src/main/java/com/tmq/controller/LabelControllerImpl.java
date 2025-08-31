package com.tmq.controller;

import com.tmq.exception.GenericExceptionHandler;
import com.tmq.exception.ObjectExistsException;
import com.tmq.exception.ObjectNotFoundException;
import com.tmq.model.Label;
import com.tmq.repository.GsonLabelRepositoryImpl;
import com.tmq.repository.LabelRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LabelControllerImpl implements LabelController {
    private static final LabelControllerImpl INSTANCE = new LabelControllerImpl();
    private static final LabelRepository REPOSITORY = GsonLabelRepositoryImpl.getInstance();

    public static LabelControllerImpl getInstance() {
        return INSTANCE;
    }

    public List<Label> getAll() {
        return REPOSITORY.findAll();
    }

    public Optional<Label> getByName(String name) {
        try {
            return Optional.of(REPOSITORY.findByName(name));
        } catch (ObjectNotFoundException e){
            return Optional.empty();
        }
    }

    public Optional<Label> getById(String id) {
        try {
            return Optional.of(REPOSITORY.findById(Long.parseLong(id)));
        } catch (ObjectNotFoundException e){
            return Optional.empty();
        }
    }

    public boolean save(Label label) {
        try {
            return REPOSITORY.save(label);
        } catch (ObjectExistsException e){
            return false;
        } catch (IOException e) {
            throw new GenericExceptionHandler(e.getMessage());
        }
    }

    @Override
    public boolean update(Label label) {
        try {
            return REPOSITORY.update(label);
        } catch (ObjectExistsException | ObjectNotFoundException e){
            return false;
        } catch (IOException e) {
            throw new GenericExceptionHandler(e.getMessage());
        }
    }

    @Override
    public boolean delete(Label label) {
        try {
            label = REPOSITORY.findById(label.getId());
            return REPOSITORY.delete(label);
        } catch (ObjectNotFoundException e){
            return false;
        } catch (IOException e) {
            throw new GenericExceptionHandler(e.getMessage());
        }
    }
}
