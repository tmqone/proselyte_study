package com.tmq.module_25.dto;

import com.tmq.module_25.entity.FileStatus;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FileDto {
    private Long id;
    private String name;
    private String location;
    private FileStatus status;
}
