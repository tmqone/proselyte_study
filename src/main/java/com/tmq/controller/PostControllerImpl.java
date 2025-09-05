package com.tmq.controller;

import com.tmq.exception.GenericExceptionHandler;
import com.tmq.exception.NotCorrectInputException;
import com.tmq.exception.PostExistsException;
import com.tmq.exception.PostNotFoundException;
import com.tmq.model.Label;
import com.tmq.model.Post;
import com.tmq.model.Status;
import com.tmq.repository.GsonPostRepositoryImpl;
import com.tmq.repository.PostRepository;
import com.tmq.validator.InputValidator;

import java.util.*;

public class PostControllerImpl implements PostController {
    private static final PostControllerImpl INSTANCE = new PostControllerImpl();
    private static final PostRepository REPOSITORY = GsonPostRepositoryImpl.getInstance();
    private static final LabelControllerImpl LABEL_CONTROLLER = LabelControllerImpl.getInstance();
    private final InputValidator inputValidator = new InputValidator();

    public static PostControllerImpl getInstance() {
        return INSTANCE;
    }

    public List<Post> getAll() {
        List<Post> posts = REPOSITORY.findAll();
        posts.forEach(post -> post.setLabels(LABEL_CONTROLLER.getActiveLabels(post.getLabels())));
        posts.forEach(REPOSITORY::update);
        return posts;
    }

    public List<Post> getByName(String name) {
        name = name.trim();
        try {
            if (!validateInput(name)) {
                return Collections.emptyList();
            }
            List<Post> posts = REPOSITORY.findByName(name).stream().toList();
            posts.forEach(post -> post.setLabels(LABEL_CONTROLLER.getActiveLabels(post.getLabels())));
            posts.forEach(REPOSITORY::update);
            return posts;
        } catch (PostNotFoundException e) {
            return Collections.emptyList();
        }
    }

    public Post getById(String id) {
        id = id.trim();
        if (!inputValidator.validateLongString(id)) {
            throw new NotCorrectInputException();
        }
        Post post = REPOSITORY.findById(Long.parseLong(id))
                .orElseThrow(PostNotFoundException::new);
        post.setLabels(LABEL_CONTROLLER.getActiveLabels(post.getLabels()));
        REPOSITORY.update(post);
        return post;
    }

    public boolean save(String title, String labels, String content) {
        if (!validateInput(title, content)) throw new NotCorrectInputException();
        labels = labels.trim();
        List<Label> labelList = Collections.emptyList();
        if (!labels.isEmpty()) labelList = LABEL_CONTROLLER.getAndSaveLabels(labels);
        return REPOSITORY.save(Post.builder()
                .title(title)
                .labels(labelList)
                .content(content)
                .status(Status.ACTIVE).build());
    }

    @Override
    public boolean update(String title, String id, String labels, String content) {
        id = id.trim();
        title = title.trim();
        labels = labels.trim();
        content = content.trim();
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

    }

    @Override
    public boolean delete(String id) {
        id = id.trim();
        if (!inputValidator.validateLongString(id)) {
            return false;
        }
        return REPOSITORY.delete(Post.builder().id(Long.parseLong(id)).status(Status.DELETED).build());
    }

    public List<Post> getByLabels(String tags) {
        tags = tags.trim();
        List<String> tagsList = Arrays.stream(tags.split("\\s*,\\s*")).toList();
        if (!validateInput(tags)) throw new GenericExceptionHandler();

        List<Post> allPosts = getAll();
        Iterator<String> allTagsIterator = tagsList.iterator();

        while (allTagsIterator.hasNext()) {
            String tag = allTagsIterator.next();
            Iterator<Post> allPostsIterator = allPosts.iterator();
            while (allPostsIterator.hasNext()) {
                Post post = allPostsIterator.next();
                List<Label> currentPostLabels = post.getLabels();
                if (currentPostLabels.isEmpty()) {
                    allPostsIterator.remove();
                    continue;
                }
                for (int i = 0; i < currentPostLabels.size(); i++) {
                    if (currentPostLabels.get(i).getName().equals(tag)) break;
                    if (!currentPostLabels.get(i).getName().equals(tag) && i == currentPostLabels.size() - 1)
                        allPostsIterator.remove();
                }
            }
        }
        return allPosts;
    }

    private boolean validateInput(String... input) {
        for (String s : input) {
            if (!inputValidator.validate(s)) return false;
        }
        return true;
    }
}
