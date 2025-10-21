package com.tmq.repository;

import com.tmq.model.Post;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

public interface PostRepository extends GenericRepository<Long, Post>{
    List<Post> findByWriter(Long writerId, Connection connection);
}
