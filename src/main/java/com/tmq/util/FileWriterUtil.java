package com.tmq.util;

import com.google.gson.Gson;
import com.tmq.exception.GenericExceptionHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileWriterUtil<T> {
    public void writeToFile(List<T> list, File file, Gson gson) {
        String jsonString = gson.toJson(list);
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(jsonString);
        } catch (IOException e) {
            throw new GenericExceptionHandler("Ошибка при записи в файл");
        }
    }
}
