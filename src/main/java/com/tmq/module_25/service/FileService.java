package com.tmq.module_25.service;

import com.tmq.module_25.entity.*;
import com.tmq.module_25.exception.FileNotFoundException;
import com.tmq.module_25.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final S3Service s3Service;
    private final FileRepository fileRepository;
    private final EventService eventService;

    @Transactional
    public Mono<FileEntity> createFile(FilePart filePart, Long userId) {
        return s3Service.uploadFile(filePart)
                .map(loc -> FileEntity.builder()
                        .name(filePart.filename())
                        .status(FileStatus.ACTIVE)
                        .location(loc)
                        .build())
                .flatMap(fileRepository::save)
                .flatMap(file ->
                        eventService.createEvent(EventEntity.builder()
                                        .fileId(file.getId())
                                        .userId(userId)
                                        .status(EventStatus.CREATED)
                                        .build()
                                )
                                .thenReturn(file)
                );
    }

    public Mono<Mono<Map<Flux<DataBuffer>, FileMetaInfo>>> downloadFile(Long id) {
        return fileRepository.findById(id)
                .map(entity -> s3Service.downloadFile(entity.getLocation()));
    }

    public Mono<FileEntity> getFileInfo(Long fileId, Long userId) {
        return fileRepository.findAllByUserId(userId, fileId)
                .switchIfEmpty(Mono.error(new FileNotFoundException("File not found")))
                .doOnSuccess(file -> log.info(file.toString()));
    }

    @Transactional
    public Mono<Boolean> deleteFile(Long id) {
        return null;
    }
}
