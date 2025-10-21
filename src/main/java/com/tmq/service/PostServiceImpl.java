package com.tmq.service;

import com.tmq.exception.GeneralException;
import com.tmq.exception.PostNotFoundException;
import com.tmq.exception.WriterNotFoundException;
import com.tmq.model.Label;
import com.tmq.model.Post;
import com.tmq.repository.*;
import com.tmq.util.DatabaseUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final LabelRepository labelRepository;
    private final WriterRepository writerRepository;
    private final PostRepository postRepository;

    @Override
    public List<Post> getAll() {
        try (Connection connection = DatabaseUtil.getConnection()) {
            return postRepository.findAll(connection).stream()
                    .peek(post -> {
                        try {
                            writerRepository.findById(post.getWriter().getId(), connection)
                                    .ifPresentOrElse(post::setWriter, () -> {
                                        throw new WriterNotFoundException();
                                    });
                            post.setLabels(labelRepository.findByPostId(post.getId(), connection));
                        } catch (SQLException e) {
                            throw new GeneralException(e);
                        }
                    }).toList();
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public List<Post> getByWriter(Long writerId) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            return postRepository.findByWriter(writerId, connection).stream()
                    .peek(post -> {
                        try {
                            post.setLabels(labelRepository.findByPostId(post.getId(), connection));
                            post.setWriter(writerRepository.findById(post.getWriter().getId(), connection)
                                    .orElseThrow(GeneralException::new));
                        } catch (SQLException e) {
                            throw new GeneralException(e);
                        }
                    }).toList();
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public Post getById(Long id) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            return postRepository.findById(id, connection).stream()
                    .peek(post -> {
                        try {
                            post.setWriter(writerRepository.findById(post.getWriter().getId(), connection)
                                    .orElseThrow(WriterNotFoundException::new));
                            post.setLabels(labelRepository.findByPostId(post.getId(), connection));
                        } catch (SQLException e) {
                            throw new GeneralException(e);
                        }
                    }).findFirst().orElseThrow(PostNotFoundException::new);
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public Post save(Post post) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Post savedPost = postRepository.save(post, connection);
                if (post.getLabels() != null && !post.getLabels().isEmpty()) {
                    for (Label l : post.getLabels()) {
                        Label savedLabel = labelRepository.save(l, connection);
                        labelRepository.saveLabelToPost(savedLabel.getId(), savedPost.getId(), connection);
                    }
                }
                savedPost.setWriter(writerRepository.findById(post.getWriter().getId(), connection)
                                                    .orElseThrow(WriterNotFoundException::new));
                connection.commit();
                return savedPost;
            } catch (SQLException | WriterNotFoundException e) {
                connection.rollback();
                throw new GeneralException(e);
            } finally {
                try {connection.setAutoCommit(true);} catch (SQLException _) {}
            }
        } catch (SQLException | GeneralException e) {
            throw new GeneralException(e);
        }
    }


    @Override
    public Post update(Post post) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            connection.setAutoCommit(false);
            try {
                post = postRepository.findById(post.getId(), connection).orElseThrow(PostNotFoundException::new);
                post.setWriter(postRepository
                        .findById(post.getId(), connection)
                        .orElseThrow(WriterNotFoundException::new)
                        .getWriter());

                Post updatedPost = postRepository.update(post, connection);

                if (post.getLabels() != null && !post.getLabels().isEmpty()) {
                    for (Label l : post.getLabels()) {
                        labelRepository.findByName(l.getName(), connection).ifPresentOrElse(x -> {},
                                () -> {
                                    try {
                                        Label savedLabel = labelRepository.save(l, connection);
                                        labelRepository.saveLabelToPost(savedLabel.getId(), updatedPost.getId(), connection);
                                    } catch (SQLException e) {
                                        throw new GeneralException(e);
                                    }
                                });
                    }
                }
                connection.commit();
                return updatedPost;
            } catch (SQLException | GeneralException e ) {
                connection.rollback();
                throw new GeneralException(e);
            } finally {
                try { connection.setAutoCommit(true);} catch (SQLException _) {}
            }
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = DatabaseUtil.getConnection()){
            if (postRepository.delete(id, connection)) return true;
            throw new PostNotFoundException();
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }
}
