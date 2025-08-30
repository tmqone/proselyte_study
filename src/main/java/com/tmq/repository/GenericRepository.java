package com.tmq.repository;

import com.google.gson.Gson;
import com.tmq.exception.ObjectNotFoundException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public interface GenericRepository<T, ID>{
    List<T> findAll();
    T findById(ID id) throws ObjectNotFoundException;
    T findByName(String name);
    boolean save(T t);
    boolean update(T t);
    boolean delete(T t);

    default void writeToFile(List<T> list, File file, Gson gson) throws IOException {
        String jsonString = gson.toJson(list);
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(jsonString);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }
}
