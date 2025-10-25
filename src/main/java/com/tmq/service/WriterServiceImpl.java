package com.tmq.service;

import com.tmq.exception.WriterNotFoundException;
import com.tmq.model.Writer;
import com.tmq.repository.WriterRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class WriterServiceImpl implements WriterService {
    private final WriterRepository writerRepository;

    @Override
    public List<Writer> getAll() {
        return writerRepository.findAll();
    }

    @Override
    public List<Writer> getByName(String firstName, String lastName) {
        return writerRepository.findByName(firstName, lastName);
    }

    @Override
    public Writer getById(Long id) {
        return writerRepository.findById(id)
                .orElseThrow(WriterNotFoundException::new);
    }

    @Override
    public Writer save(Writer writer) {
        return writerRepository.save(writer);
    }

    @Override
    public Writer update(Writer writer) {
        return writerRepository.update(writer);
    }

    @Override
    public boolean delete(Long id) {
        return writerRepository.delete(id);
    }
}
