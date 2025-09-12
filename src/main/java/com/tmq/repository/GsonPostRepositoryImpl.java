package com.tmq.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tmq.exception.GenericExceptionHandler;
import com.tmq.exception.PostNotFoundException;
import com.tmq.model.Post;
import com.tmq.model.Status;
import com.tmq.util.FileWriterUtil;
import com.tmq.util.FilesPath;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GsonPostRepositoryImpl implements PostRepository {
    private static final File FILE = new File(FilesPath.POST.getFilePath());
    private static final GsonPostRepositoryImpl INSTANCE = new GsonPostRepositoryImpl();
    private static final Gson gson = new Gson();
    private static final FileWriterUtil<Post> FILE_WRITER_UTIL = new FileWriterUtil<>();

    public static GsonPostRepositoryImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Post> findAll() {
        Type listType = new TypeToken<ArrayList<Post>>() {
        }.getType();

        try (FileReader reader = new FileReader(FILE)) {
            List<Post> posts = gson.fromJson(reader, listType);
            if (posts == null) return Collections.emptyList();
            return posts.stream()
                    .filter(post -> post.getStatus() == Status.ACTIVE)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new GenericExceptionHandler(e.getMessage());
        }
    }

    private List<Post> findAllWithDeleted() {
        Type listType = new TypeToken<ArrayList<Post>>() {
        }.getType();
        try (FileReader reader = new FileReader(FILE)) {
            List<Post> posts = gson.fromJson(reader, listType);
            if (posts == null) return Collections.emptyList();
            return posts.stream()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new GenericExceptionHandler(e.getMessage());
        }
    }

    @Override
    public Optional<Post> findById(Long id) {
        return findAll().stream()
                .filter(post -> post.getId().equals(id))
                .findFirst(); //return Post
    }

    @Override
    public List<Post> findByName(String name) {
        return findAll().stream()
                .filter(post -> post.getTitle().equalsIgnoreCase(name))
                .toList();
    }

    @Override
    public Post save(Post post) {
        List<Post> posts = findAllWithDeleted();
        post.setId(generatePostID());
        posts.add(post);
        FILE_WRITER_UTIL.writeToFile(posts, FILE, gson);
        return post;
    }

    @Override
    public Post update(Post post) {
        List<Post> posts = findAll();
        if (posts == null || posts.isEmpty()) {
            throw new PostNotFoundException();
        } else {
            List<Integer> indexes = IntStream.range(0, posts.size())
                    .filter(index -> posts.get(index).getId().equals(post.getId()))
                    .boxed().toList();

            if (indexes.size() != 1) {
                throw new PostNotFoundException();
            }
            posts.set(indexes.getFirst(), post);
            FILE_WRITER_UTIL.writeToFile(posts, FILE, gson);
            return post;
        }
    }

    @Override
    public boolean delete(Long id) {
        List<Post> posts = findAllWithDeleted();
        if (posts == null || posts.isEmpty()) {
            throw new PostNotFoundException();
        }
        int i = IntStream.range(0, posts.size())
                .filter(index -> posts.get(index).getStatus().equals(Status.ACTIVE))
                .filter(index -> posts.get(index).getId().equals(id))
                .findFirst()
                .orElseThrow(PostNotFoundException::new);
        posts.get(i).setStatus(Status.DELETED);
        FILE_WRITER_UTIL.writeToFile(posts, FILE, gson);
        return true;
    }

    private Long generatePostID(){
        return findAllWithDeleted().stream()
                .mapToLong(Post::getId)
                .max()
                .orElse(1L);
    }
}
