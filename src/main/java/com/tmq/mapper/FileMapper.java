package com.tmq.mapper;

import com.tmq.dto.entity.FileDto;
import com.tmq.dto.file.*;
import com.tmq.model.File;

import java.util.List;

public class FileMapper {

    public static FileDto toFileDto(File file){
        return new FileDto(file.getId(), file.getName(), file.getFilePath());
    }

    public File postToEntity(CreateFileRequest dto) {
        return File.builder().name(dto.name()).filePath(dto.filePath()).build();
    }

    public CreateFileResponse postFromEntity(File file) {
        return new CreateFileResponse(file.getId(), file.getName(), file.getFilePath());
    }

    public FindFileResponse getByIdFromEntity(File dto) {
        return new FindFileResponse(dto.getId(), dto.getName(), dto.getFilePath());
    }

    public List<FindAllFilesResponse> getAllFromEntity(List<File> files) {
        return files.stream().map(value -> new FindAllFilesResponse(value.getId(), value.getName(), value.getFilePath()))
                .toList();
    }

    public File updateToEntity(UpdateFileRequest dto) {
        return File.builder().id(dto.id()).name(dto.name()).filePath(dto.filePath()).build();
    }

    public UpdateFileResponse updateFromEntity(File file) {
        return new UpdateFileResponse(file.getId(), file.getName(), file.getFilePath());
    }

    public File deleteFromEntity(DeleteFileRequest dto) {
        return File.builder().id(dto.id()).build();
    }
}

