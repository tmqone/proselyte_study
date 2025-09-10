package com.tmq.controller;
import com.tmq.exception.NotCorrectInputException;
import com.tmq.exception.WriterNotFoundException;
import com.tmq.model.Post;
import com.tmq.model.Status;
import com.tmq.model.Writer;
import com.tmq.repository.GsonWriterRepositoryImpl;
import com.tmq.repository.WriterRepository;
import com.tmq.validator.InputValidator;

import java.util.Collections;
import java.util.List;

public class WriterControllerImpl implements WriterController {
    private static final WriterController INSTANCE = new WriterControllerImpl();
    private static final WriterRepository REPOSITORY = GsonWriterRepositoryImpl.getInstance();
    private final InputValidator inputValidator = new InputValidator();

    public static WriterController getInstance() {
        return INSTANCE;
    }

    public List<Writer> getAll() {
        return REPOSITORY.findAll();
    }

    public List<Writer> getByName(String lastName, String firstName) {
        lastName = lastName.trim();
        firstName = firstName.trim();
        if (!validateInput(firstName, lastName)) {
            throw new NotCorrectInputException();
        }
        List<Writer> writers = REPOSITORY.findByName(lastName, firstName);
        if (writers.isEmpty()) throw new WriterNotFoundException();
        return writers;

    }

    public Writer getById(String id) {
        id = id.trim();
        if (!inputValidator.validateLongString(id)) throw new NotCorrectInputException();

        return REPOSITORY.findById(Long.parseLong(id))
                .orElseThrow(WriterNotFoundException::new);
    }

    public boolean save(String lastName, String firstName) {
        lastName = lastName.trim();
        firstName = firstName.trim();
        if (!validateInput(lastName, firstName)) throw new NotCorrectInputException();
        return REPOSITORY.save(Writer.builder()
                .lastName(lastName)
                .firstName(firstName)
                .posts(Collections.emptyList())
                .status(Status.ACTIVE).build());
    }

    @Override
    public boolean update(String id, String lastName, String firstName) {
        lastName = lastName.trim();
        firstName = firstName.trim();
        id = id.trim();
        if (!validateInput(lastName, firstName) || !inputValidator.validateLongString(id)) {
            throw new NotCorrectInputException();
        }
        return REPOSITORY.update(Writer.builder()
                                        .lastName(lastName)
                                        .id(Long.parseLong(id))
                                        .firstName(firstName)
                                        .status(Status.ACTIVE).build());
    }

    @Override
    public boolean delete(String id) {
        id = id.trim();
        if (!inputValidator.validateLongString(id)) {
            throw new NotCorrectInputException();
        }
        return REPOSITORY.delete(Writer.builder().id(Long.parseLong(id)).status(Status.DELETED).build());
    }

    @Override
    public List<Post> getAllPostsFromAuthor(String id) {
        return List.of();
    }

    private boolean validateInput(String... input) {
        for (String s : input) {
            if (!inputValidator.validate(s)) return false;
        }
        return true;
    }
}
