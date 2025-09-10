package com.tmq.controller;
import com.tmq.exception.NotCorrectInputException;
import com.tmq.exception.PostNotFoundException;
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
    private static final PostController POST_CONTROLLER = PostControllerImpl.getInstance();
    private final InputValidator inputValidator = new InputValidator();

    public static WriterController getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Writer> getAll() {
        return REPOSITORY.findAll();
    }

    @Override
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

    @Override
    public Writer getById(String id) {
        id = id.trim();
        if (!inputValidator.validateLongString(id)) throw new NotCorrectInputException();

        return REPOSITORY.findById(Long.parseLong(id))
                .orElseThrow(WriterNotFoundException::new);
    }

    @Override
    public void save(String lastName, String firstName) {
        lastName = lastName.trim();
        firstName = firstName.trim();
        if (!validateInput(lastName, firstName)) throw new NotCorrectInputException();
        REPOSITORY.save(Writer.builder()
                .lastName(lastName)
                .firstName(firstName)
                .posts(Collections.emptyList())
                .status(Status.ACTIVE).build());
    }

    @Override
    public void save(String lastName, String firstName, List<Post> posts) {
        lastName = lastName.trim();
        firstName = firstName.trim();
        if (!validateInput(lastName, firstName)) throw new NotCorrectInputException();
        REPOSITORY.save(Writer.builder()
                .lastName(lastName)
                .firstName(firstName)
                .posts(posts)
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
                                        .posts(getAllPostsFromWriter(id))
                                        .status(Status.ACTIVE).build());
    }

    @Override
    public boolean update(String id, String lastName, String firstName, List<Post> posts) {
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
                .posts(posts)
                .status(Status.ACTIVE).build());
    }

    @Override
    public boolean delete(String id) {
        id = id.trim();
        if (!inputValidator.validateLongString(id)) {
            throw new NotCorrectInputException();
        }
        getAllPostsFromWriter(id).stream()
                .filter(post -> post.getStatus() == Status.ACTIVE)
                .forEach(post -> POST_CONTROLLER.delete(post.getId().toString()));
        return REPOSITORY.delete(Writer.builder().id(Long.parseLong(id)).status(Status.DELETED).build());
    }

    @Override
    public List<Post> getAllPostsFromWriter(String id) {
        return REPOSITORY.findById(Long.parseLong(id)).orElseThrow(WriterNotFoundException::new).getPosts();
    }

    @Override
    public boolean savePostToWriter(String authorId, Post post){
        if (!inputValidator.validateLongString(authorId))
            throw new NotCorrectInputException();

        REPOSITORY.findById(Long.parseLong(authorId))
                .ifPresentOrElse(author -> {
                    author.getPosts().add(post);
                    REPOSITORY.update(author);
                }, () -> {throw new WriterNotFoundException();});
        return true;
    }

    @Override
    public boolean updatePostInWriter(Post post){
        return getAll().stream()
                .filter(writer -> writer.getPosts().stream()
                        .anyMatch(p -> p.getId().equals(post.getId())))
                .findFirst()
                .map(writer -> {
                    writer.getPosts().replaceAll(p ->
                            p.getId().equals(post.getId()) ? post : p
                    );
                    REPOSITORY.update(writer);
                    return true;
                })
                .orElse(false);
    }

    private boolean validateInput(String... input) {
        for (String s : input) {
            if (!inputValidator.validate(s)) return false;
        }
        return true;
    }
}
