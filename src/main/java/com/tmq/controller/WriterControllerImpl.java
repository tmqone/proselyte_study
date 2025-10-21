package com.tmq.controller;

import com.tmq.dto.WriterDto;
import com.tmq.exception.NotCorrectInputException;
import com.tmq.mapper.WriterMapper;
import com.tmq.model.Writer;
import com.tmq.service.WriterService;
import com.tmq.service.WriterServiceImpl;
import com.tmq.util.InputValidator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class WriterControllerImpl implements WriterController {
    private final InputValidator inputValidator;
    private final WriterService writerService;
    private final WriterMapper writerMapper;

    @Override
    public List<WriterDto> getAll() {
        return writerService.getAll().stream().map(writerMapper::fromEntity).toList();
    }

    @Override
    public List<WriterDto> getByName(String lastName, String firstName) {
        lastName = lastName.trim();
        firstName = firstName.trim();
        if (inputValidator.validateInput(firstName, lastName)) {
            throw new NotCorrectInputException();
        }

        return writerService.getByName(firstName, lastName).stream().map(writerMapper::fromEntity).toList();
    }

    @Override
    public WriterDto getById(String id) {
        id = id.trim();
        if (inputValidator.validateLongString(id)) throw new NotCorrectInputException();

        return writerMapper.fromEntity(writerService.getById(Long.parseLong(id)));
    }

    @Override
    public WriterDto save(String lastName, String firstName) {
        lastName = lastName.trim();
        firstName = firstName.trim();
        if (inputValidator.validateInput(lastName, firstName)) throw new NotCorrectInputException();

        return writerMapper.fromEntity(writerService.save(
                Writer.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .build()
        ));
    }

    @Override
    public WriterDto update(String id, String lastName, String firstName) {
        lastName = lastName.trim();
        firstName = firstName.trim();
        id = id.trim();
        if (inputValidator.validateInput(lastName, firstName) || inputValidator.validateLongString(id)) {
            throw new NotCorrectInputException();
        }

        return writerMapper.fromEntity(writerService.update(
                Writer.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .id(Long.parseLong(id)).build()
        ));
    }

    @Override
    public boolean delete(String id) {
        id = id.trim();
        if (inputValidator.validateLongString(id)) {
            throw new NotCorrectInputException();
        }

        return writerService.delete(Long.parseLong(id));
    }
}
