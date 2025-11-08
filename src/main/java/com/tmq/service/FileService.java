package com.tmq.service;

import com.tmq.dto.event.CreateEventRequest;
import com.tmq.dto.file.FindFileRequest;
import com.tmq.dto.file.*;
import com.tmq.dto.user.FindUserByIdResponse;
import com.tmq.exception.FileNotFoundException;
import com.tmq.exception.UserNotFoundException;
import com.tmq.mapper.FileMapper;
import com.tmq.model.Action;
import com.tmq.model.File;
import com.tmq.model.User;
import com.tmq.repository.FileRepository;
import com.tmq.repository.hibernate.HibernateFileRepositoryImpl;
import com.tmq.util.FileUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileService{
    private static final FileService INSTANCE = new FileService();
    private static final FileMapper fileMapper = new FileMapper();
    private static final FileRepository fileRepository = HibernateFileRepositoryImpl.getInstance();
    private static final UserService userService = UserService.getInstance();
    private static final EventService eventService = EventService.getInstance();

    public static FileService getInstance() {
        return INSTANCE;
    }

    public CreateFileResponse save(CreateFileRequest request) {
        FindUserByIdResponse user = userService.findById(request.userId());
        Map<String, Path> savedFileOnDisk = FileUtil.saveFiles(request.parts(), request.userId());
        List<File> filesToSaveInDatabase = new ArrayList<>();

        savedFileOnDisk.forEach((key, value) -> {
            filesToSaveInDatabase.add(fileMapper.postToEntity(request, value.toString(), key));
        });

        List<File> result = filesToSaveInDatabase.stream().map(fileRepository::save).toList();
        result.forEach(file -> eventService.save(new CreateEventRequest(user.id(), file.getId(), Action.UPLOAD)));
        return fileMapper.postFromEntity(result);
    }

    public UpdateFileResponse update(UpdateFileRequest request) {
        userService.existsById(request.userId());
        File file = fileRepository.findById(request.id()).orElseThrow(FileNotFoundException::new);
        Map<String, Path> stringPathMap = FileUtil.updateFile(Path.of(file.getFilePath()), request.name());
        file.setFilePath(String.valueOf(stringPathMap.get(request.name())));
        file.setName(request.name());
        File userResponse = fileRepository.update(file);
        eventService.save(new CreateEventRequest(request.userId(), userResponse.getId(), Action.UPDATED));
        return fileMapper.updateFromEntity(userResponse);
    }

    public boolean delete(DeleteFileRequest deleteFileRequest) {
        FindUserByIdResponse user = userService.findById(deleteFileRequest.userId());
        File file = fileRepository.findById(deleteFileRequest.id(), deleteFileRequest.userId())
                .orElseThrow(FileNotFoundException::new);
        FileUtil.deleteFile(Path.of(file.getFilePath()));
        boolean delete = fileRepository.delete(deleteFileRequest.id(), deleteFileRequest.userId());
        eventService.save(new CreateEventRequest(user.id(), file.getId(), Action.DELETE));
        return delete;
    }

    public FindFileResponse findById(FindFileRequest request) {
//        if (!userService.existsById(request.userId())) throw new UserNotFoundException();
        File file = fileRepository.findById(request.id(), request.userId()).orElseThrow(FileNotFoundException::new);
//        eventService.save(new CreateEventRequest(user.id(), file.getId(), Action.GET));
        return fileMapper.getByIdFromEntity(file);
    }

    public List<FindAllFilesResponse> findAll(Integer userId) {
        List<File> files = fileRepository.findAll(userId);
//        files.forEach(file -> eventService.save(new CreateEventRequest(userId, file.getId(), Action.GET)));
        return fileMapper.getAllFromEntity(files);
    }

    public boolean existsById(Integer id){
        return fileRepository.existsById(id);
    }
}
