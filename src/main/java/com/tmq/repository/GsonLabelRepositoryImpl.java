package com.tmq.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tmq.exception.*;
import com.tmq.model.Label;
import com.tmq.model.Status;
import com.tmq.util.FileWriterUtil;
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
public class GsonLabelRepositoryImpl implements LabelRepository {
    private static final File FILE = new File(FilesPath.LABEL.getFilePath());
    private static final GsonLabelRepositoryImpl INSTANCE = new GsonLabelRepositoryImpl();
    private static final Gson gson = new Gson();
    private static final FileWriterUtil<Label> FILE_WRITER_UTIL = new FileWriterUtil();

    public static GsonLabelRepositoryImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Label> findAll() {
        Type listType = new TypeToken<ArrayList<Label>>() {
        }.getType();

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

    private List<Label> findAllWithDeleted() {
        Type listType = new TypeToken<ArrayList<Label>>() {
        }.getType();
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
    public Label save(Label label) {
        List<Label> allLabels = findAllWithDeleted();
        if (!allLabels.isEmpty()) {
            allLabels.stream()
                    .filter(value -> value.getName().equals(label.getName()))
                    .filter(value -> value.getStatus() == Status.ACTIVE)
                    .findAny()
                    .ifPresent(value -> {
                        throw new LabelExistsException(value.getName() + " already exists");
                    });
        }

        label.setId(generateLabelID());
        allLabels.add(label);
        FILE_WRITER_UTIL.writeToFile(allLabels, FILE, gson);
        return label;
    }

    @Override
    public Label update(Label label) {
        List<Label> allLabels = findAllWithDeleted();
        if (allLabels == null || allLabels.isEmpty()) {
            throw new LabelNotFoundException();
        }
        List<Integer> indexes = IntStream.range(0, allLabels.size())
                .filter(index -> allLabels.get(index).getStatus() == Status.ACTIVE)
                .filter(index -> allLabels.get(index).getId().equals(label.getId()))
                .boxed().toList();

        if (indexes.isEmpty()) {
            throw new LabelNotFoundException();
        }

        if (indexes.size() > 1) {
            throw new LabelExistsException();
        }

        allLabels.set(indexes.getFirst(), label);
        FILE_WRITER_UTIL.writeToFile(allLabels, FILE, gson);
        return label;
    }

    @Override
    public boolean delete(Long id) {
        List<Label> allLabels = findAllWithDeleted();
        if (allLabels == null || allLabels.isEmpty()) {
            throw new LabelNotFoundException();
        }

        int i = IntStream.range(0, allLabels.size())
                .filter(index -> allLabels.get(index).getId().equals(id))
                .findFirst()
                .orElseThrow(LabelNotFoundException::new);
        allLabels.get(i).setStatus(Status.DELETED);
        FILE_WRITER_UTIL.writeToFile(allLabels, FILE, gson);
        return true;
    }

    private Long generateLabelID() {
        return findAllWithDeleted().stream()
                .mapToLong(Label::getId)
                .max()
                .orElse(1L);
    }
}
