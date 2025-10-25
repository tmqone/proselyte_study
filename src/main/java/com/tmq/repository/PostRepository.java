package com.tmq.repository;

import com.tmq.model.Post;

import java.util.List;

public interface PostRepository extends GenericRepository<Long, Post>{
    List<Post> findByWriter(Long writerId);
}
