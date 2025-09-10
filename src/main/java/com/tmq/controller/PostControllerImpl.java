package com.tmq.controller;

import com.tmq.exception.GenericExceptionHandler;
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

    public List<Post> getAll() {
        return REPOSITORY.findAll();
    }

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

    public Post getById(String id) {
        id = id.trim();
        if (!inputValidator.validateLongString(id)) {
            throw new NotCorrectInputException();
        }
        return REPOSITORY.findById(Long.parseLong(id)).orElseThrow(PostNotFoundException::new);
    }

    public void save(String writerId, String title, String labels, String content) {
        if (!validateInput(title, content) || !inputValidator.validateLongString(writerId)) throw new NotCorrectInputException();
        labels = labels.trim();
        List<Label> labelList = Collections.emptyList();
        if (!labels.isEmpty()) labelList = LABEL_CONTROLLER.getAndSaveLabels(labels);
        Post post = Post.builder()
                .title(title)
                .labels(labelList)
                .content(content)
                .status(Status.ACTIVE).build();
        Long id = REPOSITORY.save(post);
        post.setId(id);
        WRITER_CONTROLLER.savePostToWriter(writerId, post);
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
        if (REPOSITORY.update(post)) {
            WRITER_CONTROLLER.updatePostInWriter(post);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        id = id.trim();
        if (!inputValidator.validateLongString(id)) {
            return false;
        }
        Post post = getById(id);
        post.setStatus(Status.DELETED);
        if (REPOSITORY.delete(post)) {
            WRITER_CONTROLLER.updatePostInWriter(post);
            return true;
        }
        return false;
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

    @Override
    public boolean updateLabelInPosts(Label label){
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
