package com.tmq.repository;

import com.tmq.model.Label;
import com.tmq.model.Writer;

import java.util.List;
import java.util.Optional;

public class GsonWriterRepositoryImpl implements WriterRepository{
    @Override
    public List<Writer> findAll() {
        return List.of();
    }

    @Override
    public List<Writer> findAllWithDeleted() {
        return List.of();
    }

    @Override
    public Optional<Writer> findById(Long aLong) {
        return null;
    }

    @Override
    public Optional<Writer> findByName(String name) {
        return null;
    }

    @Override
    public boolean save(Writer writer) {
        return false;
    }

    @Override
    public boolean update(Writer writer) {
        return false;
    }

    @Override
    public boolean delete(Writer writer) {
        return false;
    }
}
