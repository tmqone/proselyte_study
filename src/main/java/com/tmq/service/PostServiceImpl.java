package com.tmq.service;

import com.tmq.exception.GeneralException;
import com.tmq.exception.PostNotFoundException;
import com.tmq.exception.WriterNotFoundException;
import com.tmq.model.Label;
import com.tmq.model.Post;
import com.tmq.model.PostStatus;
import com.tmq.repository.*;
import com.tmq.util.DatabaseUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final LabelService labelService;
    @Override
    public List<Post> getAll() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> getByWriter(Long writerId) {
        return postRepository.findByWriter(writerId);
    }

    @Override
    public Post getById(Long id) {
        return postRepository.findById(id).orElseThrow(PostNotFoundException::new);
    }

    @Override
    public Post save(Post post) {
        post.setCreated(Instant.now());
        post.setUpdated(Instant.now());
        post.setPostStatus(PostStatus.ACTIVE);
        return postRepository.save(post);
    }


    @Override
    public Post update(Post post) {
        post.setUpdated(Instant.now());
        post.setWriter(postRepository.findById(post.getId()).orElseThrow(PostNotFoundException::new).getWriter());
        return postRepository.update(post);
    }

    @Override
    public boolean delete(Long id) {
        if (postRepository.delete(id)) return true;
        throw new PostNotFoundException();
    }
}
