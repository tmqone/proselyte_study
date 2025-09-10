package com.tmq.controller;

import com.tmq.model.Post;
import com.tmq.model.Writer;
import com.tmq.view.GenericView;

import java.util.List;

public interface WriterController extends GenericController<Writer> {
    List<Writer> getAll();
    List<Writer> getByName(String lastName, String firstName);
    Writer getById(String id);
    boolean save(String lastName, String firstName);
    boolean update(String id, String lastName, String firstName);
    boolean delete(String name);
    List<Post> getAllPostsFromAuthor(String name);
}
