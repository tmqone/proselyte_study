package com.tmq.module_25.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "events")
public class EventEntity {
    @Id
    private Long id;
    @Column("user_id")
    private UserEntity user;
    @Column("file_id")
    private FileEntity file;
    private EventStatus status;
    private Date timestamp;
}
