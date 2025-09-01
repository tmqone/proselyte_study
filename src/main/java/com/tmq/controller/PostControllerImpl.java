package com.tmq.controller;

import com.tmq.exception.GenericExceptionHandler;
import com.tmq.exception.ObjectExistsException;
import com.tmq.exception.ObjectNotFoundException;
import com.tmq.model.Label;
import com.tmq.model.Post;
import com.tmq.model.Status;
import com.tmq.repository.GsonPostRepositoryImpl;
import com.tmq.repository.PostRepository;
import com.tmq.validator.InputValidator;

import java.io.IOException;
import java.util.*;

public class PostControllerImpl implements PostController {
    private static final PostControllerImpl INSTANCE = new PostControllerImpl();
    private static final PostRepository REPOSITORY = GsonPostRepositoryImpl.getInstance();
    private static final LabelControllerImpl LABEL_CONTROLLER = LabelControllerImpl.getInstance();
    private final InputValidator inputValidator = new InputValidator();
    private static final String NOT_FOUND_MESSAGE = "Label not found";

    public static PostControllerImpl getInstance() {
        return INSTANCE;
    }

    public List<Post> getAll() {
        return REPOSITORY.findAll();
    }

    public List<Post> getByName(String name) {
        name = name.trim();
        try {
            if (!validateInput(name)) {
                return Collections.emptyList();
            }
            return REPOSITORY.findByName(name).stream()
                    .toList();
        } catch (ObjectNotFoundException e) {
            return Collections.emptyList();
        }
    }

    public Optional<Post> getById(String id) {
        id = id.trim();
        try {
            if (!inputValidator.validateLongString(id)) {
                return Optional.empty();
            }
            return Optional.of(REPOSITORY.findById(Long.parseLong(id)))
                    .orElseThrow(() -> new ObjectNotFoundException(NOT_FOUND_MESSAGE));
        } catch (ObjectNotFoundException e) {
            return Optional.empty();
        }
    }

    public boolean save(String title, String labels, String content) {
        if(!validateInput(title, labels, content)) return false;
        List<Label> labelList = LABEL_CONTROLLER.getAndSaveLabels(labels);
        try {
            return REPOSITORY.save(Post.builder()
                    .title(title)
                    .labels(labelList)
                    .content(content)
                    .status(Status.ACTIVE).build());
        } catch (ObjectExistsException e) {
            return false;
        } catch (IOException e) {
            throw new GenericExceptionHandler(e.getMessage());
        }
    }

    private boolean validateInput(String... input) {
        for (String s : input) {
            if (!inputValidator.validate(s)) return false;
        }
        return true;
    }

    @Override
    public boolean update(String id, String title, String labels, String content) {
        id = id.trim();
        title = title.trim();
        labels = labels.trim();
        content = content.trim();

        try {
            if (!validateInput(title, labels, content) || !inputValidator.validateLongString(id)) {
                return false;
            }
            return REPOSITORY.update(Post
                    .builder()
                    .title(title)
                    .labels(LABEL_CONTROLLER.getAndSaveLabels(labels))
                    .content(content)
                    .id(Long.parseLong(id))
                    .status(Status.ACTIVE).build());

        } catch (ObjectExistsException | ObjectNotFoundException | NumberFormatException e) {
            return false;
        } catch (IOException e) {
            throw new GenericExceptionHandler(e.getMessage());
        }
    }

    @Override
    public boolean delete(String id) {
        id = id.trim();
        try {
            if (!inputValidator.validateLongString(id)) {
                return false;
            }
            Post post = REPOSITORY.findById(Long.parseLong(id))
                    .orElseThrow(() -> new ObjectNotFoundException(NOT_FOUND_MESSAGE));
            post.setStatus(Status.DELETED);
            return REPOSITORY.delete(post);
        } catch (ObjectNotFoundException | NumberFormatException e) {
            return false;
        } catch (IOException e) {
            throw new GenericExceptionHandler(e.getMessage());
        }
    }

    public List<Post> getByLabels(String tags) {
        tags = tags.trim();
        List<String> tagsList = Arrays.stream(tags.split(",")).toList();
        if (!validateInput(tags)) return Collections.emptyList();

        List<Post> posts = getAll();
        Iterator<Post> iterator = posts.iterator();

        while (iterator.hasNext()) {
            Post nextPost = iterator.next();
            List<Label> labelList = nextPost.getLabels();
            for (Label label : labelList) {
                if (!tagsList.contains(label.getName())) iterator.remove();
            }
        }

        return posts;
    }
}
