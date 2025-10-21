package com.tmq.repository;

import com.tmq.exception.GeneralException;
import com.tmq.model.Post;
import com.tmq.model.PostStatus;
import com.tmq.model.Writer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tmq.sql.PostRepositorySql.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostRepositoryImpl implements PostRepository {
    private static final PostRepository INSTANCE = new PostRepositoryImpl();

    public static PostRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Post> findAll(Connection connection) {
        try {
            ResultSet resultSet = connection.prepareStatement(FIND_ALL_SQL).executeQuery();
            List<Post> posts = new ArrayList<>();
            while (resultSet.next()) {
                posts.add(Post.builder()
                        .id(resultSet.getLong("id"))
                        .content(resultSet.getString("content"))
                        .writer(Writer.builder().id(resultSet.getLong("writer_id")).build())
                        .created(resultSet.getTimestamp("created_at").toInstant())
                        .updated(resultSet.getTimestamp("updated_at").toInstant())
                        .postStatus(PostStatus.valueOf(resultSet.getString("status")))
                        .build());
            }
            return posts;
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public Optional<Post> findById(Long id, Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(Post.builder()
                        .id(resultSet.getLong("id"))
                        .content(resultSet.getString("content"))
                        .writer(Writer.builder().id(resultSet.getLong("writer_id")).build())
                        .created(resultSet.getTimestamp("created_at").toInstant())
                        .updated(resultSet.getTimestamp("updated_at").toInstant())
                        .postStatus(PostStatus.valueOf(resultSet.getString("status")))
                        .build());
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public List<Post> findByWriter(Long writerId, Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_WRITER_SQL);
            preparedStatement.setLong(1, writerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Post> posts = new ArrayList<>();
            while (resultSet.next()) {
                posts.add(Post.builder()
                        .id(resultSet.getLong("id"))
                        .postStatus(PostStatus.valueOf(resultSet.getString("status")))
                        .content(resultSet.getString("content"))
                        .writer(Writer.builder().id(resultSet.getLong("writer_id")).build())
                        .updated(resultSet.getTimestamp("updated_at").toInstant())
                        .created(resultSet.getTimestamp("created_at").toInstant())
                        .build()
                );
            }
            return posts;

        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public Post save(Post post, Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_POST_SQL, Statement.RETURN_GENERATED_KEYS);
            post.setCreated(Instant.now());
            post.setUpdated(Instant.now());
            post.setPostStatus(PostStatus.ACTIVE);
            preparedStatement.setString(1, post.getContent());
            preparedStatement.setLong(2, post.getWriter().getId());
            preparedStatement.setTimestamp(3, Timestamp.from(post.getUpdated()));
            preparedStatement.setTimestamp(4, Timestamp.from(post.getCreated()));
            preparedStatement.setString(5, post.getPostStatus().name());
            preparedStatement.executeUpdate();
            preparedStatement.getGeneratedKeys().next();
            long id = preparedStatement.getGeneratedKeys().getLong("id");
            post.setId(id);
            return post;
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public Post update(Post post, Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL);
            post.setUpdated(Instant.now());
            preparedStatement.setString(1, post.getContent());
            preparedStatement.setLong(2, post.getWriter().getId());
            preparedStatement.setTimestamp(3, Timestamp.from(post.getUpdated()));
            preparedStatement.setString(4, post.getPostStatus().name());
            preparedStatement.setLong(5, post.getId());
            preparedStatement.executeUpdate();
            return post;
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public boolean delete(Long id, Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID_SQL);
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }
}
