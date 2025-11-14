package com.tmq.service;

import com.tmq.dto.event.CreateEventRequest;
import com.tmq.dto.file.FindFileRequest;
import com.tmq.dto.file.*;
import com.tmq.dto.user.FindUserByIdResponse;
import com.tmq.exception.FileNotFoundException;
import com.tmq.mapper.FileMapper;
import com.tmq.model.Action;
import com.tmq.model.File;
import com.tmq.repository.FileRepository;
import com.tmq.repository.hibernate.HibernateFileRepositoryImpl;
import com.tmq.util.FileUtil;
import com.tmq.util.HibernateUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileService {
    private static final FileService INSTANCE = new FileService();
    private static final FileMapper fileMapper = new FileMapper();
    private static final FileRepository fileRepository = HibernateFileRepositoryImpl.getInstance();
    private static final UserService userService = UserService.getInstance();
    private static final EventService eventService = EventService.getInstance();

    public static FileService getInstance() {
        return INSTANCE;
    }

    public CreateFileResponse save(CreateFileRequest request) {
        return HibernateUtil.handleRequest(() -> {
            FindUserByIdResponse user = userService.findById(request.userId());
            Map<String, Path> savedFileOnDisk = FileUtil.saveFiles(request.parts(), request.userId());
            List<File> filesToSaveInDatabase = new ArrayList<>();

            savedFileOnDisk.forEach((key, value) -> {
                filesToSaveInDatabase.add(fileMapper.postToEntity(request, value.toString(), key));
            });

            List<File> result = filesToSaveInDatabase.stream().map(fileRepository::save).toList();
            result.forEach(file -> eventService.insert(new CreateEventRequest(user.id(), file.getId(), Action.UPLOAD)));
            return fileMapper.postFromEntity(result);
        });
    }

    public UpdateFileResponse update(UpdateFileRequest request) {
        return HibernateUtil.handleRequest(() -> {
            File file = fileRepository.findByUserId(request.userId())
                    .stream()
                    .filter(result -> result.getId().equals(request.id()))
                    .findFirst()
                    .orElseThrow(FileNotFoundException::new);
            Map<String, Path> stringPathMap = FileUtil.updateFile(Path.of(file.getFilePath()), request.name());
            file.setFilePath(String.valueOf(stringPathMap.get(request.name())));
            file.setName(request.name());
            File userResponse = fileRepository.update(file);
            eventService.insert(new CreateEventRequest(request.userId(), userResponse.getId(), Action.UPDATED));
            return fileMapper.updateFromEntity(userResponse);
        });
    }

    public boolean delete(DeleteFileRequest deleteFileRequest) {
        return HibernateUtil.handleRequest(() -> {
            FindUserByIdResponse user = userService.findById(deleteFileRequest.userId());
            File file = fileRepository.findByUserId(deleteFileRequest.userId())
                    .stream()
                    .filter(result -> result.getId().equals(deleteFileRequest.id()))
                    .findFirst()
                    .orElseThrow(FileNotFoundException::new);
            FileUtil.deleteFile(Path.of(file.getFilePath()));
            boolean delete = fileRepository.delete(deleteFileRequest.id());
            eventService.insert(new CreateEventRequest(user.id(), file.getId(), Action.DELETE));
            return delete;
        });
    }

    public UpdateFileResponse updateByAdmin(UpdateFileRequest request, Integer adminId) {
        return HibernateUtil.handleRequest(() -> {
            File file = fileRepository.findByUserId(request.userId())
                    .stream()
                    .filter(result -> result.getId().equals(request.id()))
                    .findFirst()
                    .orElseThrow(FileNotFoundException::new);
            Map<String, Path> stringPathMap = FileUtil.updateFile(Path.of(file.getFilePath()), request.name());
            file.setFilePath(String.valueOf(stringPathMap.get(request.name())));
            file.setName(request.name());
            File userResponse = fileRepository.update(file);
            eventService.insert(new CreateEventRequest(adminId, userResponse.getId(), Action.UPDATED));
            return fileMapper.updateFromEntity(userResponse);
        });
    }

    public boolean deleteByAdmin(Integer id, Integer userId, Integer adminId) {
        return HibernateUtil.handleRequest(() -> {
            FindUserByIdResponse user = userService.findById(userId);
            File file = fileRepository.findById(id)
                    .orElseThrow(FileNotFoundException::new);
            FileUtil.deleteFile(Path.of(file.getFilePath()));
            boolean delete = fileRepository.delete(id);
            eventService.insert(new CreateEventRequest(adminId, file.getId(), Action.DELETE));
            return delete;
        });
    }

    public FindFileResponse findById(FindFileRequest request) {
        return HibernateUtil.handleRequest(() -> {
            File file = fileRepository.findByUserId(request.userId())
                    .stream()
                    .filter(result -> result.getId().equals(request.id()))
                    .findFirst()
                    .orElseThrow(FileNotFoundException::new);
            eventService.insert(new CreateEventRequest(request.userId(), file.getId(), Action.GET));
            return fileMapper.getByIdFromEntity(file);
        });
    }

    public FindFileResponse findByIdNoEvent(Integer id) {
        return HibernateUtil.handleRequest(() ->
                fileMapper.getByIdFromEntity(fileRepository.findById(id).orElseThrow(FileNotFoundException::new)));
    }

    public List<FindAllFilesResponse> findByUserIdNoEvent(Integer userId) {
        return HibernateUtil.handleRequest(() ->
                fileMapper.getAllFromEntity(fileRepository.findByUserId(userId)));
    }

    public List<FindAllFilesResponse> findAllFilesByUserId(Integer userId) {
        return HibernateUtil.handleRequest(() -> {
            List<File> files = fileRepository.findAllByUserId(userId);
            List<CreateEventRequest> list = files.stream()
                    .map(file -> new CreateEventRequest(userId, file.getId(), Action.GET))
                    .toList();
            eventService.insert(list);
            return fileMapper.getAllFromEntity(files);
        });
    }

    public byte[] downloadFile(DownloadFileRequest request) {
        return HibernateUtil.handleRequest(() -> {
            File file = fileRepository.findByUserId(request.userId())
                    .stream()
                    .filter(result -> result.getId().equals(request.id()))
                    .findFirst()
                    .orElseThrow(FileNotFoundException::new);
            byte[] result = FileUtil.getFile(Path.of(file.getFilePath()));
            eventService.insert(new CreateEventRequest(request.userId(), request.id(), Action.DOWNLOAD));
            return result;
        });
    }

    public List<FindAllFilesResponse> findAll() {
        return HibernateUtil.handleRequest(() -> fileMapper.getAllFromEntity(fileRepository.findAll()));
    }
}
