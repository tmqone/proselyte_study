package com.tmq.controller;

import com.tmq.dto.PostDto;
import com.tmq.exception.NotCorrectInputException;
import com.tmq.mapper.PostMapper;
import com.tmq.model.Label;
import com.tmq.model.Post;
import com.tmq.model.PostStatus;
import com.tmq.model.Writer;
import com.tmq.service.PostService;
import com.tmq.service.PostServiceImpl;
import com.tmq.util.InputValidator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
public class PostControllerImpl implements PostController {
    private final PostService postService;
    private final PostMapper postMapper;
    private final InputValidator inputValidator;

    @Override
    public List<PostDto> getAll() {
        return postService.getAll().stream().map(postMapper::fromEntity).toList();
    }

    @Override
    public PostDto getById(String id) {
        id = id.trim();
        if (inputValidator.validateLongString(id)) {
            throw new NotCorrectInputException();
        }
        return postMapper.fromEntity(postService.getById(Long.parseLong(id)));
    }

    @Override
    public List<PostDto> getByWriter(String id) {
        id = id.trim();
        if (inputValidator.validateLongString(id)) throw new NotCorrectInputException();
        return postService.getByWriter(Long.parseLong(id)).stream().map(postMapper::fromEntity).toList();
    }

    @Override
    public PostDto save(String writerId, String labels, String content) {
        labels = labels.trim();
        if (inputValidator.validateInput(content, labels) || inputValidator.validateLongString(writerId)) {
            throw new NotCorrectInputException();
        }

        List<String> labelsList = Arrays.asList(labels.split(","));

        return postMapper.fromEntity(postService.save(Post.builder()
                .writer(Writer.builder().id(Long.valueOf(writerId)).build())
                .content(content)
                .labels(labelsList.stream().map(label -> new Label(0L, label)).toList())
                .build()));
    }

    @Override
    public PostDto update(String id, String labels, String content) {
        id = id.trim();
        labels = labels.trim();
        content = content.trim();
        if (inputValidator.validateInput(content, labels) || inputValidator.validateLongString(id)) {
            throw new NotCorrectInputException();
        }

        List<String> labelsList = Arrays.asList(labels.split(","));
        return postMapper.fromEntity(postService.update(Post.builder()
                .content(content)
                .labels(labelsList.stream().map(label -> Label.builder().name(label).build()).toList())
                .id(Long.valueOf(id))
                .postStatus(PostStatus.ACTIVE)
                .build()));
    }

    @Override
    public boolean delete(String id) {
        id = id.trim();
        if (inputValidator.validateLongString(id)) throw new NotCorrectInputException();
        return postService.delete(Long.parseLong(id));
    }
}
