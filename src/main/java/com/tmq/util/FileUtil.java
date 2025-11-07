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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    public static List<CreateFileRequest> saveFiles(Collection<Part> parts, Integer userId){
        // TODO вынести валидацию
        if (parts.size() == 0) throw new NotCorrectInputException("В теле запроса отсутствует поле file");
        verifyUserFolder(userId);
        List<CreateFileRequest> files = new ArrayList<>();
        parts.forEach(part -> {
            try (InputStream inputStream = part.getInputStream()) {
                Path path = generateUploadFilePath(userId, part);
                Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
                files.add(new CreateFileRequest(userId, part.getSubmittedFileName(), path.toString()));
            } catch (IOException e) {
                throw new GeneralException("Ошибка при сохранении файла");
            }
        });
        return files;
    }

    public static UpdateFileRequest updateFile(Integer userId, Integer id, Collection<Part> parts) {
        verifyUserFolder(userId);
        // TODO вынести валидацию в отдельный класс
        if (parts.size() == 0) throw new NotCorrectInputException("В теле запроса отсутствует поле file");
        if (parts.size() > 1) throw new NotCorrectInputException("Добавлен больше чем один файл в запрос");
        Part part = parts.stream().findFirst().orElseThrow(NotCorrectInputException::new);
        try (InputStream inputStream = part.getInputStream()){
            Path path = generateUploadFilePath(userId, part);
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            return new UpdateFileRequest(id, userId, part.getSubmittedFileName(), path.toString());
        } catch (IOException e) {
            throw new GeneralException("Ошибка при обновлении файла");
        }
    }
}
