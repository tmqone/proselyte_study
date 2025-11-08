package com.tmq.dto.file;

import com.tmq.dto.entity.FileDto;
import java.util.List;

public record CreateFileResponse (List<FileDto> files){
}
