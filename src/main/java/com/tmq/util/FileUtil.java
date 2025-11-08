package com.tmq.util;

import com.tmq.dto.file.CreateFileRequest;
import com.tmq.dto.file.UpdateFileRequest;
import com.tmq.exception.FileExistsException;
import com.tmq.exception.GeneralException;
import com.tmq.exception.NotCorrectInputException;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class FileUtil {
    public static void initUploadFolder(){
        File file = new File(PropertiesUtil.get("file.path"));
        if(!file.exists()){
            file.mkdirs();
        }
    }

    private static void verifyUserFolder(Integer userId) {
        File file = new File(PropertiesUtil.get("file.path").concat("/").concat(userId.toString()));
        if (!file.exists()) file.mkdirs();
    }

    private static Path generateUploadFilePath(Integer userId, Part part) {
        return Path.of(PropertiesUtil.get("file.path")
                .concat("/")
                .concat(userId.toString())
                .concat("/")
                .concat(part.getSubmittedFileName()));
    }

    public static Map<String, Path> saveFiles(Collection<Part> parts, Integer userId){
        verifyUserFolder(userId);
        Map<String, Path> fileMap = new HashMap<>();
        parts.forEach(part -> {
            try (InputStream inputStream = part.getInputStream()) {
                Path path = generateUploadFilePath(userId, part);
                if(Files.exists(path)) throw new FileExistsException();
                Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
                fileMap.put(part.getSubmittedFileName(), path);
            } catch (FileExistsException e ) {
                fileMap.forEach((s, path) -> {
                    try {
                        Files.delete(path);
                    } catch (IOException ex) {
                        throw new GeneralException("Ошибка при удалении файлов при неудачном запросе");
                    }
                });
                throw new FileExistsException();
            } catch (IOException e) {
                throw new GeneralException("Ошибка при сохранении файла");
            }
        });
        return fileMap;
    }

    public static void deleteFile(Path path){
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new GeneralException(e);
        }
    }

    public static Map<String, Path> updateFile(Path oldPath, String newFileName){
        try {
            Path newPath = Path.of(oldPath.getParent().toString() + "/" + newFileName);
            if (Files.exists(newPath)) throw new FileExistsException();
            Files.copy(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
            deleteFile(oldPath);
            return Map.of(newFileName, newPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] getFile(Path path){
        try {
            byte[] bytes = Files.readAllBytes(path);
            return bytes;
        } catch (IOException e) {
            throw new GeneralException("Ошибка при чтении файла");
        }
    }
}
