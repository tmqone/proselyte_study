package com.tmq.repository;

import com.tmq.model.Post;

import java.util.List;

public class GsonPostRepositoryImpl implements PostRepository{
    @Override
    public List<Post> findAll() {
        return List.of();
    }

    @Override
    public Post findById(Long aLong) {
        return null;
    }

    @Override
    public Post findByName(String name) {
        return null;
    }

    @Override
    public boolean save(Post post) {
        return false;
    }

    @Override
    public boolean update(Post post) {
        return false;
    }

    @Override
    public boolean delete(Post post) {
        return false;
    }
}
