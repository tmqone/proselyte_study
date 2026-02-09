package com.tmq.module_25.service;

import com.tmq.module_25.entity.FileEntity;
import com.tmq.module_25.entity.FileMetaInfo;
import com.tmq.module_25.entity.FileStatus;
import com.tmq.module_25.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileService {
    private final S3Service s3Service;
    private final FileRepository fileRepository;

    @Transactional
    public Mono<FileEntity> createFile(FilePart filePart) {
        return s3Service.uploadFile(filePart)
                .doOnNext(loc -> System.out.println("S3 returned location = [" + loc + "]"))
                .map(loc -> FileEntity.builder()
                        .name(filePart.filename())
                        .status(FileStatus.ACTIVE)
                        .location(loc)
                        .build())
                .doOnNext(e -> System.out.println("Entity before save location = [" + e.getLocation() + "]"))
                .flatMap(fileRepository::save)
                .doOnNext(e -> System.out.println("Entity after save location = [" + e.getLocation() + "]"));
    }

    public Mono<Mono<Map<Flux<DataBuffer>, FileMetaInfo>>> downloadFile(Long id) {
        return fileRepository.findById(id)
                .map(entity -> s3Service.downloadFile(entity.getLocation()));
    }

    public Mono<FileEntity> getFileInfo(Long id) {
        return fileRepository.findById(id);
    }

    @Transactional
    public Mono<Boolean> deleteFile(Long id) {
        return null;
    }
}
