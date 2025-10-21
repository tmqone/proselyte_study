package com.tmq.controller;

import com.tmq.dto.WriterDto;
import com.tmq.model.Post;
import com.tmq.model.Writer;

import java.util.List;

public interface WriterController extends GenericController<WriterDto> {
    List<WriterDto> getAll();

    List<WriterDto> getByName(String lastName, String firstName);

    WriterDto getById(String id);

    WriterDto save(String lastName, String firstName);

    WriterDto update(String id, String lastName, String firstName);

    boolean delete(String name);
}
