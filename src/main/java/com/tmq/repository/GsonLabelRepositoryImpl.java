package com.tmq.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tmq.Main;
import com.tmq.exception.ObjectExistsException;
import com.tmq.exception.ObjectNotFoundException;
import com.tmq.model.Label;
import com.tmq.model.Status;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GsonLabelRepositoryImpl implements LabelRepository  {
    private static final File PATH = new File(System.getProperty("user.home") + "\\.tmq\\postingApp");
    private static final File FILE = new File(PATH + "\\labels.json");
    private static final GsonLabelRepositoryImpl INSTANCE = new GsonLabelRepositoryImpl();
    private static final Gson gson = new Gson();

    static {
        if (!PATH.exists()) {
            PATH.mkdirs();
        }

        if (!FILE.exists()) {
            try {
                boolean newFile = FILE.createNewFile();
                System.out.println(newFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static GsonLabelRepositoryImpl getInstance() {
        return INSTANCE;
    }

    @SneakyThrows
    public List<Label> findAll() {
        Type listType = new TypeToken<ArrayList<Label>>() {}.getType();
        List<Label> labels = gson.fromJson(new FileReader(FILE), listType);
        return Optional.ofNullable(labels)
                .filter(value -> !value.isEmpty())
                .filter(value -> value.removeIf(label -> label.getStatus() == Status.DELETED))
                .orElse(Collections.emptyList());
    }

    @Override
    @SneakyThrows
    public Label findById(Long id) {
        return findAll().stream()
                .filter(label -> label.getId().equals(id))
                .filter(label -> label.getStatus() == Status.ACTIVE)
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("Label not found"));
    }

    @SneakyThrows
    @Override
    public Label findByName(String name) {
        return findAll().stream()
                .filter(label -> label.getName().equalsIgnoreCase(name))
                .filter(label -> label.getStatus() == Status.ACTIVE)
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("Label not found"));
    }

    @Override
    @SneakyThrows
    public boolean save(Label label) {
        List<Label> allLabels = findAll();
        if (allLabels == null || allLabels.isEmpty()) {
            label.setId(1L);
            writeToFile(List.of(label), FILE, gson);
            return true;
        } else {

            allLabels.stream()
                    .filter(value -> value.getName().equalsIgnoreCase(label.getName()))
                    .filter(value -> value.getStatus() == Status.ACTIVE)
                    .findAny()
                    .ifPresent(value -> {
                        throw new ObjectExistsException(value.getName() + " already exists");
                    });

            Long id = allLabels.getLast().getId();
            label.setId(id + 1L);
            allLabels.add(label);
            writeToFile(allLabels, FILE, gson);
            return true;
        }
    }

    @Override
    @SneakyThrows
    public boolean update(Label label){
        List<Label> allLabels = findAll();
        if (allLabels == null || allLabels.isEmpty()) {
            throw new ObjectNotFoundException("Label not found");
        } else {
            List<Integer> indexes = IntStream.range(0, allLabels.size())
                    .filter(index -> allLabels.get(index).getId().equals(label.getId()))
                    .filter(index -> allLabels.get(index).getStatus() == Status.ACTIVE)
                    .boxed().toList();

            if (indexes.isEmpty()) {
                throw new ObjectNotFoundException("Label not found");
            }
            if (indexes.size() > 1) {
                throw new ObjectNotFoundException("More than one active label with same id");
            }

            allLabels.set(indexes.get(0), label);
            writeToFile(allLabels, FILE, gson);
            return true;
        }
    }

    @Override
    @SneakyThrows
    public boolean delete(Label label) {
        List<Label> allLabels = findAll();
        if (allLabels == null || allLabels.isEmpty()) {
            throw new ObjectNotFoundException("Label not found");
        } else {
            int i = IntStream.range(0, allLabels.size())
                    .filter(index -> allLabels.get(index).equals(label))
                    .filter(index -> allLabels.get(index).getStatus() == Status.ACTIVE)
                    .findFirst()
                    .orElseThrow(() -> new ObjectNotFoundException("Label not found"));
            allLabels.get(i).setStatus(Status.DELETED);
            writeToFile(allLabels, FILE, gson);
            return true;
        }
    }



}
