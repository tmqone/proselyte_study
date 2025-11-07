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

import java.util.List;

public class FileService{
    private final FileMapper fileMapper = new FileMapper();
    private final FileRepository fileRepository = new HibernateFileRepositoryImpl();
    private final UserService userService = new UserService();
    private final EventService eventService = new EventService();

    public CreateFileResponse save(CreateFileRequest request) {
        FindUserByIdResponse user = userService.findById(request.userId());
        File file = fileMapper.postToEntity(request);
        File save = fileRepository.save(file);
        eventService.save(new CreateEventRequest(user.id(), file.getId(), Action.UPLOAD));
        return fileMapper.postFromEntity(save);
    }

    public UpdateFileResponse update(UpdateFileRequest request) {
        FindUserByIdResponse user = userService.findById(request.userId());
        File file = fileMapper.updateToEntity(request);
        eventService.save(new CreateEventRequest(user.id(), file.getId(), Action.UPDATED));
        File save = fileRepository.update(file);
        return fileMapper.updateFromEntity(save);
    }

    public boolean delete(DeleteFileRequest deleteFileRequest) {
        return fileRepository.delete(deleteFileRequest.id());
    }

    public FindFileResponse findById(FindFileRequest request) {
        FindUserByIdResponse user = userService.findById(request.userId());
        File file = fileRepository.findById(request.id()).orElseThrow(FileNotFoundException::new);
        eventService.save(new CreateEventRequest(user.id(), file.getId(), Action.DOWNLOAD));
        return fileMapper.getByIdFromEntity(file);
    }

    public List<FindAllFilesResponse> findAll() {
        return fileMapper.getAllFromEntity(fileRepository.findAll());
    }
}
