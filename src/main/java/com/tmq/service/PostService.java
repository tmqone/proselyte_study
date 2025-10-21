package com.tmq.service;

import com.tmq.dto.PostDto;
import com.tmq.model.Post;
import com.tmq.model.Writer;

import java.sql.SQLException;
import java.util.List;

public interface PostService extends GenericService<Post> {
    List<Post> getAll();

    List<Post> getByWriter(Long writerId);

    Post getById(Long id);

    Post save(Post post);

    Post update(Post post);

    boolean delete(Long id);
}
