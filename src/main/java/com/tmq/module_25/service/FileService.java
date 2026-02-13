package com.tmq.module_25.service;

import com.tmq.module_25.entity.*;
import com.tmq.module_25.exception.FileNotFoundException;
import com.tmq.module_25.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {
    private final S3Service s3Service;
    private final FileRepository fileRepository;
    private final EventService eventService;

    @Value("${aws.external_url}")
    private String minioURL;
    @Value("${aws.port}")
    private String minioPort;
    @Value("${aws.bucket}")
    private String minioBucket;

    @Transactional
    public Mono<FileEntity> createFile(FilePart filePart, Long userId) {
        log.info("User id = {} uploading file '{}'", userId, filePart.filename());
        return s3Service
                .uploadFile(filePart)
                .map(loc -> FileEntity.builder()
                        .name(filePart.filename())
                        .status(FileStatus.ACTIVE)
                        .location("%s:%s/%s/%s".formatted(minioURL, minioPort, minioBucket, loc))
                        .build())
                .flatMap(fileRepository::save)
                .flatMap(file ->
                        eventService.createEvent(EventEntity.builder()
                                        .fileId(file.getId())
                                        .userId(userId)
                                        .status(EventStatus.CREATED)
                                        .timestamp(LocalDateTime.now())
                                        .build()
                                )
                                .thenReturn(file)
                )
                .doOnSuccess(file -> log.info("User id = {} uploaded file {}", userId, file));
    }

    public Mono<FileEntity> getFileInfo(Long fileId, Long userId) {
        log.info("User id = {} getting info about file id = {}", userId, fileId);
        return fileRepository.findFileWithUserId(userId, fileId)
                .switchIfEmpty(Mono.error(new FileNotFoundException("File not found")))
                .doOnSuccess(file ->
                        log.info("User id = {} got info about file id = {}", userId, fileId));
    }

    public Flux<FileEntity> getAllFileInfoByUserId(Long userId) {
        return fileRepository.findAllByUserId(userId);
    }

    public Flux<FileEntity> getAll() {
        return fileRepository.findAll();
    }


    @Transactional
    public Mono<Void> deleteFileByUser(Long id, Long userId) {
        return fileRepository.findFileWithUserId(userId, id)
                .switchIfEmpty(Mono.error(new FileNotFoundException("File not found exception")))
                .doOnNext(file -> log.info("User id = {} deleting file id = {}", userId, file.getId()))
                .flatMap(file ->
                        fileRepository.deleteById(file.getId())
                                .thenReturn(file)
                )
                .flatMap(file -> eventService.createEvent(EventEntity.builder()
                        .fileId(file.getId())
                        .userId(userId)
                        .status(EventStatus.DELETED)
                        .timestamp(LocalDateTime.now())
                        .build())
                        .thenReturn(file))
                .doOnNext(file -> log.info("User id={} archived file id={}", userId, file.getId()))
                .then();
    }

    @Transactional
    public Mono<Void> deleteFileByAdmin(Long id, Long userId) {
        return fileRepository
                .deleteById(id)
                .doOnNext(file -> log.info("User id = {} deleting file id = {}", userId, id))
                .flatMap(file -> eventService.createEvent(EventEntity.builder()
                                .fileId(id)
                                .userId(userId)
                                .status(EventStatus.DELETED)
                                .timestamp(LocalDateTime.now())
                                .build())
                        .thenReturn(file))
                .doOnNext(file -> log.info("User id={} archived file id={}", userId, id))
                .then();
    }


}
