package com.tmq.module_25.service;

import com.tmq.module_25.entity.FileDownloadEntity;
import com.tmq.module_25.entity.FileMetaInfo;
import com.tmq.module_25.exception.AWSException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.core.async.ResponsePublisher;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    @Value("${aws.bucket}")
    private String bucket;

    private final S3AsyncClient s3AsyncClient;

    public Mono<String> uploadFile (FilePart filePart) {
        try {
            Path tmp = Files.createTempFile("upload-", "-" + filePart.filename());
            String id = UUID.randomUUID().toString();

            MediaType ct = filePart.headers().getContentType();
            String contentType = (ct != null) ? ct.toString() : MediaType.APPLICATION_OCTET_STREAM_VALUE;

            return filePart.transferTo(tmp)
                    .then(Mono.fromCallable(() -> Files.size(tmp)))
                    .flatMap(size -> {
                        PutObjectRequest req = PutObjectRequest.builder()
                                .bucket(bucket)
                                .key(id)
                                .contentType(contentType)
                                .contentLength(size)
                                .contentDisposition("attachment")
                                .build();

                        return Mono.fromFuture(() ->
                                s3AsyncClient.putObject(req, AsyncRequestBody.fromFile(tmp)));
                    })
                    .map(obj -> id)
                    .doFinally(sig -> {
                        try { Files.deleteIfExists(tmp); } catch (IOException _) {}
                    });
        } catch (IOException e) {
            throw new AWSException(e.getMessage(), "UPLOAD_FILE_ERROR");
        }
    }
}
