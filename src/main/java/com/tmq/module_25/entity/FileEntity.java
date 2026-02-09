package com.tmq.module_25.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "files")
public class FileEntity {
    @Id
    private Long id;
    private String name;
    private String location;
    @Column("status")
    private FileStatus status;
    private EventEntity events;
}
