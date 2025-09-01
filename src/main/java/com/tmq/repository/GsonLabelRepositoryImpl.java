package com.tmq.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tmq.exception.GenericExceptionHandler;
import com.tmq.exception.ObjectExistsException;
import com.tmq.exception.ObjectNotFoundException;
import com.tmq.model.Label;
import com.tmq.model.Status;
import com.tmq.util.FilesPath;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GsonLabelRepositoryImpl implements LabelRepository  {
    private static final File FILE = new File(FilesPath.LABEL.getFilePath());
    private static final GsonLabelRepositoryImpl INSTANCE = new GsonLabelRepositoryImpl();
    private static final Gson gson = new Gson();
    private static final String NOT_FOUND_MESSAGE = "Label not found";

    public static GsonLabelRepositoryImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Label> findAll() {
        Type listType = new TypeToken<ArrayList<Label>>() {}.getType();

        try (FileReader reader = new FileReader(FILE)) {
            List<Label> labels = gson.fromJson(reader, listType);
            if (labels == null) return Collections.emptyList();
            return labels.stream()
                    .filter(label -> label.getStatus() == Status.ACTIVE)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new GenericExceptionHandler(e.getMessage());
        }
    }

    @Override
    public List<Label> findAllWithDeleted() {
        Type listType = new TypeToken<ArrayList<Label>>() {}.getType();
        try (FileReader reader = new FileReader(FILE)) {
            List<Label> labels = gson.fromJson(reader, listType);
            if (labels == null) return Collections.emptyList();
            return labels.stream()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new GenericExceptionHandler(e.getMessage());
        }
    }

    @Override
    public Optional<Label> findById(Long id) {
        return findAll().stream()
                .filter(label -> label.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Label> findByName(String name) {
        return findAll().stream()
                .filter(label -> label.getName().equals(name))
                .findFirst();
    }

    @Override
    public boolean save(Label label) {
        List<Label> allLabels = findAllWithDeleted();
        if (allLabels == null || allLabels.isEmpty()) {
            label.setId(1L);
            writeToFile(List.of(label), FILE, gson);
            return true;
        } else {
            allLabels.stream()
                    .filter(value -> value.getName().equals(label.getName()))
                    .filter(value -> value.getStatus() == Status.ACTIVE)
                    .findAny()
                    .ifPresent(value -> {
                        throw new ObjectExistsException(value.getName() + " already exists");
                    });

            Long newId = allLabels.stream().mapToLong(Label::getId).max().getAsLong() + 1;
            label.setId(newId);
            allLabels.add(label);
            writeToFile(allLabels, FILE, gson);
            return true;
        }
    }

    @Override
    public boolean update(Label label) {
        List<Label> allLabels = findAll();
        if (allLabels == null || allLabels.isEmpty()) {
            throw new ObjectNotFoundException(NOT_FOUND_MESSAGE);
        } else {
            List<Integer> indexes = IntStream.range(0, allLabels.size())
                    .filter(index -> allLabels.get(index).getId().equals(label.getId()))
                    .boxed().toList();

            if (indexes.isEmpty()) {
                throw new ObjectNotFoundException(NOT_FOUND_MESSAGE);
            }
            if (indexes.size() > 1) {
                throw new ObjectNotFoundException("More than one active label with same id");
            }
            allLabels.set(indexes.getFirst(), label);
            writeToFile(allLabels, FILE, gson);
            return true;
        }
    }

    //TODO Если лейбл был удален через меню лейблов, то он должен быть и удален в постах
    @Override
    public boolean delete(Label label) {
        List<Label> allLabels = findAllWithDeleted();
        if (allLabels == null || allLabels.isEmpty()) {
            throw new ObjectNotFoundException(NOT_FOUND_MESSAGE);
        } else {
            int i = IntStream.range(0, allLabels.size())
                    .filter(index -> allLabels.get(index).getId().equals(label.getId()))
                    .findFirst()
                    .orElseThrow(() -> new ObjectNotFoundException(NOT_FOUND_MESSAGE));
            allLabels.get(i).setStatus(Status.DELETED);
            writeToFile(allLabels, FILE, gson);
            return true;
        }
    }
}
