package com.tmq.repository;

import com.tmq.model.Writer;

import java.util.List;

public class GsonWriterRepositoryImpl implements WriterRepository{
    @Override
    public List<Writer> findAll() {
        return List.of();
    }

    @Override
    public Writer findById(Long aLong) {
        return null;
    }

    @Override
    public Writer findByName(String name) {
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
