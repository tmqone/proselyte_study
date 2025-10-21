package com.tmq.service;

import com.tmq.model.Writer;

import java.util.List;

public interface WriterService extends GenericService<Writer>{
    List<Writer> getAll();

    List<Writer> getByName(String firstName, String lastName);

    Writer getById(Long id);

    Writer save(Writer writer);

    Writer update(Writer Writer);

    boolean delete(Long id);
}
