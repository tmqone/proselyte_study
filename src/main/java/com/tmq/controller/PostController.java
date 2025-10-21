package com.tmq.controller;

import com.tmq.dto.PostDto;
import com.tmq.model.Label;
import com.tmq.model.Post;

import java.util.List;

public interface PostController extends GenericController<PostDto> {
    List<PostDto> getAll();

    PostDto getById(String id);

    List<PostDto> getByWriter(String id);

    PostDto save(String writerId, String name, String labels);

    PostDto update(String id, String labels, String content);

    boolean delete(String id);
}
