package com.tmq.module_25.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;


@Data
@Builder
public class FileDownloadEntity {
    private Flux<DataBuffer> data;
    private FileMetaInfo metaInfo;
}
