package com.tmq.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tmq.exception.GenericExceptionHandler;
import com.tmq.exception.PostNotFoundException;
import com.tmq.exception.WriterNotFoundException;
import com.tmq.model.Label;
import com.tmq.model.Post;
import com.tmq.model.Status;
import com.tmq.model.Writer;
import com.tmq.util.FilesPath;
import lombok.SneakyThrows;

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

public class GsonWriterRepositoryImpl implements WriterRepository{
    private static final File FILE = new File(FilesPath.WRITER.getFilePath());
    private static final WriterRepository INSTANCE = new GsonWriterRepositoryImpl();
    private static final Gson gson = new Gson();

    public static WriterRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Writer> findAll() {
        Type listType = new TypeToken<ArrayList<Writer>>() {}.getType();

        try (FileReader reader = new FileReader(FILE)) {
            List<Writer> writers = gson.fromJson(reader, listType);
            if (writers == null) return Collections.emptyList();
            return writers.stream()
                    .filter(post -> post.getStatus() == Status.ACTIVE)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new GenericExceptionHandler(e.getMessage());
        }
    }

    @Override
    public List<Writer> findAllWithDeleted() {
        Type listType = new TypeToken<ArrayList<Writer>>() {}.getType();
        try (FileReader reader = new FileReader(FILE)) {
            List<Writer> writers = gson.fromJson(reader, listType);
            if (writers == null) return Collections.emptyList();
            return writers.stream()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new GenericExceptionHandler(e.getMessage());
        }
    }

    @Override
    public Optional<Writer> findById(Long id) {
        return findAll().stream()
                .filter(writer -> writer.getId().equals(id))
                .findFirst();
    }

    public List<Writer> findByName(String lastName, String firstName) {
        return findAll().stream()
                .filter(writer -> writer.getLastName().equalsIgnoreCase(lastName))
                .filter(writer -> writer.getFirstName().equalsIgnoreCase(firstName))
                .toList();
    }

    @Override
    public Long save(Writer writer) {
        List<Writer> writers = findAllWithDeleted();
        if (writers == null || writers.isEmpty()) {
            writer.setId(1L);
            writeToFile(List.of(writer), FILE, gson);
            return 1L;
        } else {
            Long newId = writers.stream().mapToLong(Writer::getId).max().getAsLong() + 1;
            writer.setId(newId);
            writers.add(writer);
            writeToFile(writers, FILE, gson);
            return newId;
        }
    }

    @Override
    public boolean update(Writer writer) {
        List<Writer> writers = findAll();
        if (writers == null || writers.isEmpty()) {
            throw new WriterNotFoundException();
        } else {
            List<Integer> indexes = IntStream.range(0, writers.size())
                    .filter(index -> writers.get(index).getId().equals(writer.getId()))
                    .boxed().toList();

            if (indexes.isEmpty()) {
                throw new WriterNotFoundException();
            }
            if (indexes.size() > 1) {
                throw new WriterNotFoundException();
            }
            writers.set(indexes.getFirst(), writer);
            writeToFile(writers, FILE, gson);
            return true;
        }
    }

    @Override
    public boolean delete(Writer writer) {
        List<Writer> writers = findAllWithDeleted();
        if (writers == null || writers.isEmpty()) {
            throw new WriterNotFoundException();
        } else {
            int i = IntStream.range(0, writers.size())
                    .filter(index -> writers.get(index).getId().equals(writer.getId()))
                    .findFirst()
                    .orElseThrow(WriterNotFoundException::new);
            writers.get(i).setStatus(Status.DELETED);
            writeToFile(writers, FILE, gson);
            return true;
        }
    }
}
