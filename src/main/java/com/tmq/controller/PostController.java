package com.tmq.controller;

import com.tmq.model.Label;
import com.tmq.model.Post;

import java.util.List;

public interface PostController extends GenericController<Post> {
    List<Post> getAll();
    List<Post> getByName(String name);
    Post getById(String id);
    void save(String writerId, String name, String label, String content);
    boolean update(String id, String title, String labels, String content);
    boolean delete(String id);
    List<Post> getByLabels(String label);
    boolean updateLabelInPosts(Label label);
}
