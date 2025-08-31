package com.tmq.controller;

import com.tmq.model.Label;
import com.tmq.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostController extends GenericController<Post> {
    List<Post> getAll();
    List<Post> getByName(String name);
    Optional<Post> getById(String id);
    boolean save(String name, String label, String content);
    boolean update(String id, String title, String labels, String content);
    boolean delete(String name);
}
