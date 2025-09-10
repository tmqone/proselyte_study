package com.tmq.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tmq.exception.GenericExceptionHandler;
import com.tmq.exception.PostNotFoundException;
import com.tmq.model.Post;
import com.tmq.model.Status;
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

public class GsonPostRepositoryImpl implements PostRepository{
    private static final File FILE = new File(FilesPath.POST.getFilePath());
    private static final GsonPostRepositoryImpl INSTANCE = new GsonPostRepositoryImpl();
    private static final Gson gson = new Gson();

    public static GsonPostRepositoryImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Post> findAll() {
        Type listType = new TypeToken<ArrayList<Post>>() {}.getType();

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

    @Override
    public List<Post> findAllWithDeleted() {
        Type listType = new TypeToken<ArrayList<Post>>() {}.getType();
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
                .filter(Post -> Post.getId().equals(id))
                .findFirst();
    }

    public List<Post> findByName(String name) {
        return findAll().stream()
                .filter(post -> post.getTitle().equalsIgnoreCase(name))
                .toList();
    }

    @Override
    public Long save(Post post) {
        List<Post> posts = findAllWithDeleted();
        if (posts == null || posts.isEmpty()) {
            post.setId(1L);
            writeToFile(List.of(post), FILE, gson);
            return 1L;
        } else {
            Long newId = posts.stream().mapToLong(Post::getId).max().getAsLong() + 1;
            post.setId(newId);
            posts.add(post);
            writeToFile(posts, FILE, gson);
            return newId;
        }
    }

    @Override
    public boolean update(Post post) {
        List<Post> posts = findAll();
        if (posts == null || posts.isEmpty()) {
            throw new PostNotFoundException();
        } else {
            List<Integer> indexes = IntStream.range(0, posts.size())
                    .filter(index -> posts.get(index).getId().equals(post.getId()))
                    .boxed().toList();

            if (indexes.isEmpty()) {
                throw new PostNotFoundException();
            }
            if (indexes.size() > 1) {
                throw new PostNotFoundException();
            }
            posts.set(indexes.getFirst(), post);
            writeToFile(posts, FILE, gson);
            return true;
        }
    }

    @Override
    public boolean delete(Post post) {
        List<Post> posts = findAllWithDeleted();
        if (posts == null || posts.isEmpty()) {
            throw new PostNotFoundException();
        } else {
            int i = IntStream.range(0, posts.size())
                    .filter(index -> posts.get(index).getId().equals(post.getId()))
                    .findFirst()
                    .orElseThrow(PostNotFoundException::new);
            posts.get(i).setStatus(Status.DELETED);
            writeToFile(posts, FILE, gson);
            return true;
        }
    }
}
