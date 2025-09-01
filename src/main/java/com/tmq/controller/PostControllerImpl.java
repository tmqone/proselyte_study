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
    private static final String NOT_FOUND_MESSAGE = "Тэг не найден";
    private static final String NOT_CORRECT_INPUT = "Некорретный ввод";
    private static final String ALREADY_EXISTS_MESSAGE = "Некорретный ввод";

    public static PostControllerImpl getInstance() {
        return INSTANCE;
    }

    public List<Post> getAll() {
        List<Post> posts = REPOSITORY.findAll();
        for (Post post : posts) {
            post.setLabels(LABEL_CONTROLLER.getActiveLabels(post.getLabels()));
        }
        posts.forEach(REPOSITORY::update);
        return posts;
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

    public Post getById(String id) {
        id = id.trim();
        if (!inputValidator.validateLongString(id)) {
            throw new GenericExceptionHandler(NOT_FOUND_MESSAGE);
        }
        return REPOSITORY.findById(Long.parseLong(id))
                .orElseThrow(() -> new ObjectNotFoundException(NOT_FOUND_MESSAGE));

    }

    public boolean save(String title, String labels, String content) {
        if (!validateInput(title, content)) throw new GenericExceptionHandler(NOT_CORRECT_INPUT);
        labels = labels.trim();
        List<Label> labelList = Collections.emptyList();
        if (!labels.isEmpty()) labelList = LABEL_CONTROLLER.getAndSaveLabels(labels);
        try {
            return REPOSITORY.save(Post.builder()
                    .title(title)
                    .labels(labelList)
                    .content(content)
                    .status(Status.ACTIVE).build());
        } catch (ObjectExistsException e) {
            throw new GenericExceptionHandler(ALREADY_EXISTS_MESSAGE);
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
            if (!validateInput(title, content) || !inputValidator.validateLongString(id)) {
                return false;
            }
            return REPOSITORY.update(Post
                    .builder()
                    .title(title)
                    .labels(LABEL_CONTROLLER.getAndSaveLabels(labels))
                    .content(content)
                    .id(Long.parseLong(id))
                    .status(Status.ACTIVE).build());

        } catch (ObjectExistsException | ObjectNotFoundException e) {
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
            return REPOSITORY.delete(Post.builder().id(Long.parseLong(id)).status(Status.DELETED).build());
        } catch (ObjectNotFoundException e) {
            throw new GenericExceptionHandler(NOT_FOUND_MESSAGE);
        }
    }

    public List<Post> getByLabels(String tags) {
        tags = tags.trim();
        List<String> tagsList = Arrays.stream(tags.split("\\s*,\\s*")).toList();
        if (!validateInput(tags)) throw new GenericExceptionHandler(NOT_CORRECT_INPUT);

        List<Post> allPosts = getAll();
        Iterator<String> allTagsIterator = tagsList.iterator();

        while (allTagsIterator.hasNext()) {
            String tag = allTagsIterator.next();
            Iterator<Post> allPostsIterator = allPosts.iterator();
            while (allPostsIterator.hasNext()) {
                Post post = allPostsIterator.next();
                List<Label> currentPostLabels = post.getLabels();
                for (int i = 0; i < currentPostLabels.size(); i++) {
                    if (currentPostLabels.get(i).getName().equals(tag)) break;
                    if (!currentPostLabels.get(i).getName().equals(tag) && i == currentPostLabels.size() - 1) allPostsIterator.remove();
                }
            }
        }
        return allPosts;
    }


}
