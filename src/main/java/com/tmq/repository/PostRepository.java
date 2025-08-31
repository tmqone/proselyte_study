package com.tmq.repository;

import com.tmq.model.Post;

import java.util.List;

public interface PostRepository extends GenericRepository<Post, Long> {
    List<Post> findByName(String title);
}
