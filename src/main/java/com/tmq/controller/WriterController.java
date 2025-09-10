package com.tmq.controller;

import com.tmq.model.Post;
import com.tmq.model.Writer;

import java.util.List;

public interface WriterController extends GenericController<Writer> {
    List<Writer> getAll();

    List<Writer> getByName(String lastName, String firstName);

    Writer getById(String id);

    void save(String lastName, String firstName);

    void save(String lastName, String firstName, List<Post> posts);

    boolean update(String id, String lastName, String firstName);

    boolean update(String id, String lastName, String firstName, List<Post> posts);

    boolean delete(String name);

    List<Post> getAllPostsFromWriter(String name);

    boolean savePostToWriter(String authorId, Post post);

    boolean updatePostInWriter(Post post);
}
