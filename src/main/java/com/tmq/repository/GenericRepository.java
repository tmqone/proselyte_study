package com.tmq.repository;

import com.google.gson.Gson;
import com.tmq.exception.GenericExceptionHandler;
import com.tmq.exception.ObjectNotFoundException;
import com.tmq.model.Label;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface GenericRepository<T, ID>{
    List<T> findAll();
    List<T> findAllWithDeleted();
    Optional<T> findById(ID id);
    boolean save(T t);
    boolean update(T t);
    boolean delete(T t);

    default void writeToFile(List<T> list, File file, Gson gson) {
        String jsonString = gson.toJson(list);
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(jsonString);
        } catch (IOException e) {
            throw new GenericExceptionHandler("Ошибка при записи в файл");
        }
    }
}
