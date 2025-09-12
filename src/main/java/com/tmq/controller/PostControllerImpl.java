package com.tmq.controller;

import com.tmq.exception.NotCorrectInputException;
import com.tmq.exception.PostNotFoundException;
import com.tmq.model.Label;
import com.tmq.model.Post;
import com.tmq.model.Status;
import com.tmq.repository.GsonPostRepositoryImpl;
import com.tmq.repository.PostRepository;
import com.tmq.validator.InputValidator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostControllerImpl implements PostController {
    private static final PostControllerImpl INSTANCE = new PostControllerImpl();
    private static final WriterController WRITER_CONTROLLER = WriterControllerImpl.getInstance();
    private static final PostRepository REPOSITORY = GsonPostRepositoryImpl.getInstance();
    private static final LabelControllerImpl LABEL_CONTROLLER = LabelControllerImpl.getInstance();
    private final InputValidator inputValidator = new InputValidator();

    public static PostControllerImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Post> getAll() {
        return REPOSITORY.findAll();
    }

    @Override
    public List<Post> getByName(String name) {
        name = name.trim();
        try {
            if (!validateInput(name)) {
                return Collections.emptyList();
            }
            return REPOSITORY.findByName(name);
        } catch (PostNotFoundException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Post getById(String id) {
        id = id.trim();
        if (!inputValidator.validateLongString(id)) {
            throw new NotCorrectInputException();
        }
        return REPOSITORY.findById(Long.parseLong(id)).orElseThrow(PostNotFoundException::new);
    }

    @Override
    public void save(String writerId, String title, String labels, String content) {
        labels = labels.trim();
        if (!validateInput(title, content) || !inputValidator.validateLongString(writerId))
            throw new NotCorrectInputException();
        List<Label> labelList = Collections.emptyList();
        if (!labels.isEmpty()) labelList = LABEL_CONTROLLER.getAndSaveLabels(labels);
        Post post = Post.builder()
                .title(title)
                .labels(labelList)
                .content(content)
                .status(Status.ACTIVE).build();
        Post savedPost = REPOSITORY.save(post);
        WRITER_CONTROLLER.savePostToWriter(writerId, savedPost);
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
        Post post = Post.builder()
                .title(title)
                .labels(LABEL_CONTROLLER.getAndSaveLabels(labels))
                .content(content)
                .id(Long.parseLong(id))
                .status(Status.ACTIVE).build();
        REPOSITORY.update(post);
        WRITER_CONTROLLER.updatePostInWriter(post);
        return true;
    }

    @Override
    public boolean delete(String id) {
        id = id.trim();
        if (!inputValidator.validateLongString(id)) {
            return false;
        }
        Post post = getById(id);
        post.setStatus(Status.DELETED);
        REPOSITORY.delete(Long.parseLong(id));
        WRITER_CONTROLLER.updatePostInWriter(post);
        return true;
    }

    @Override
    public boolean updateLabelInPosts(Label label) {
        getAll().forEach(post -> {
            post.getLabels()
                    .replaceAll(labelInStream -> labelInStream.getId().equals(label.getId()) ? label : labelInStream);
            REPOSITORY.update(post);
            WRITER_CONTROLLER.updatePostInWriter(post);
        });
        return true;
    }

    private boolean validateInput(String... input) {
        for (String s : input) {
            if (!inputValidator.validate(s)) return false;
        }
        return true;
    }
}
