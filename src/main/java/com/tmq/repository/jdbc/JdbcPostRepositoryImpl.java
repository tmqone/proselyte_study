package com.tmq.repository.jdbc;

import com.tmq.exception.GeneralException;
import com.tmq.model.Label;
import com.tmq.model.Post;
import com.tmq.model.PostStatus;
import com.tmq.model.Writer;
import com.tmq.repository.LabelRepository;
import com.tmq.repository.PostRepository;
import com.tmq.util.DatabaseUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.*;

import static com.tmq.sql.LabelRepositorySql.SAVE_LABEL_TO_POST;
import static com.tmq.sql.PostRepositorySql.*;
import static com.tmq.sql.LabelRepositorySql.SAVE_LABEL_SQL;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JdbcPostRepositoryImpl implements PostRepository {
    private static final PostRepository INSTANCE = new JdbcPostRepositoryImpl();

    public static PostRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Post> findAll() {
        try (PreparedStatement preparedStatement = DatabaseUtil.getStatement(FIND_ALL_SQL)) {
            ResultSet rs = preparedStatement.executeQuery();
            Map<Long, Post> postById = new LinkedHashMap<>();

            while (rs.next()) {
                long id = rs.getLong("post_id");
                if (!postById.containsKey(id)) {
                    postById.put(id, mapResultSetToPost(rs, id));
                }
                postById.get(id).getLabels().add(new Label(rs.getLong("label_id"), rs.getString("label_name")));
            }
            return List.copyOf(postById.values());
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public Optional<Post> findById(Long id) {
        try (PreparedStatement preparedStatement = DatabaseUtil.getStatement(FIND_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.next()) {
                return Optional.empty();
            }
            long postId = rs.getLong("post_id");
            Post post = mapResultSetToPost(rs, postId);
            do {
                post.getLabels().add(new Label(rs.getLong("label_id"), rs.getString("label_name")));
            } while (rs.next());

            return Optional.of(post);
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public List<Post> findByWriter(Long writerId) {
        try (PreparedStatement preparedStatement = DatabaseUtil.getStatement(FIND_BY_WRITER_SQL)) {
            preparedStatement.setLong(1, writerId);
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.next()) return Collections.emptyList();
            Map<Long, Post> postById = new LinkedHashMap<>();

            do {
                long id = rs.getLong("post_id");
                if (!postById.containsKey(id)) {
                    postById.put(id, mapResultSetToPost(rs, id));
                }
                postById.get(id).getLabels().add(new Label(rs.getLong("label_id"), rs.getString("label_name")));
            } while (rs.next());

            return List.copyOf(postById.values());
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public Post save(Post post) {
        try (PreparedStatement saveLabelToPostStatement = DatabaseUtil.getStatementWithGeneratedKeys(SAVE_LABEL_TO_POST);
             PreparedStatement savePostStatement = DatabaseUtil.getStatementWithGeneratedKeys(SAVE_POST_SQL);
             PreparedStatement saveLabelStatement = DatabaseUtil.getStatementWithGeneratedKeys(SAVE_LABEL_SQL)) {

            savePostStatement.setString(1, post.getContent());
            savePostStatement.setLong(2, post.getWriter().getId());
            savePostStatement.setTimestamp(3, Timestamp.from(post.getCreated()));
            savePostStatement.setTimestamp(4, Timestamp.from(post.getUpdated()));
            savePostStatement.setString(5, post.getPostStatus().name());
            savePostStatement.executeUpdate();
            ResultSet postKey = savePostStatement.getGeneratedKeys();
            postKey.next();
            post.setId(postKey.getLong("id"));

            for (Label label : post.getLabels()) {
                saveLabelStatement.setString(1, label.getName());
                saveLabelStatement.executeUpdate();
                ResultSet generatedKeys = saveLabelStatement.getGeneratedKeys();
                generatedKeys.next();
                label.setId(generatedKeys.getLong("id"));
            }

            for (Label label : post.getLabels()) {
                saveLabelToPostStatement.setLong(1, post.getId());
                saveLabelToPostStatement.setLong(2, label.getId());
                saveLabelToPostStatement.executeUpdate();
            }

            // Коммит один, т.к. все стейтменты висят на одном коннекте
            saveLabelStatement.getConnection().commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return post;
    }

    @Override
    public Post update(Post post) {
        try (PreparedStatement updatePostStatement = DatabaseUtil.getStatement(UPDATE_SQL);
             PreparedStatement updateLabelsStatement = DatabaseUtil.getStatementWithGeneratedKeys(SAVE_LABEL_SQL);
             PreparedStatement updateLabelToPostStatement = DatabaseUtil.getStatement(SAVE_LABEL_TO_POST);
             PreparedStatement removeLabelToPostStatement = DatabaseUtil.getStatement(DELETE_LABEL_FROM_POST)) {

            for (Label label : post.getLabels()) {
                updateLabelsStatement.setString(1, label.getName());
                updateLabelsStatement.executeUpdate();
                ResultSet generatedKeys = updateLabelsStatement.getGeneratedKeys();
                generatedKeys.next();
                label.setId(generatedKeys.getLong("id"));
            }

            removeLabelToPostStatement.setLong(1, post.getId());
            removeLabelToPostStatement.executeUpdate();

            updatePostStatement.setString(1, post.getContent());
            updatePostStatement.setLong(2, post.getWriter().getId());
            updatePostStatement.setTimestamp(3, Timestamp.from(post.getUpdated()));
            updatePostStatement.setString(4, post.getPostStatus().name());
            updatePostStatement.setLong(5, post.getId());
            updatePostStatement.executeUpdate();

            for (Label label : post.getLabels()) {
                updateLabelToPostStatement.setLong(1, post.getId());
                updateLabelToPostStatement.setLong(2, label.getId());
                updateLabelToPostStatement.executeUpdate();
            }

            // Коммит один, т.к. все стейтменты висят на одном коннекте
            updatePostStatement.getConnection().commit();
            return post;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(Long id) {
        try (PreparedStatement preparedStatement = DatabaseUtil.getStatement(DELETE_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);
            preparedStatement.getConnection().commit();
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    private static Post mapResultSetToPost(ResultSet rs, Long postId) throws SQLException {
        return Post.builder()
                .id(postId)
                .content(rs.getString("content"))
                .writer(Writer.builder()
                        .id(rs.getLong("writer_id"))
                        .firstName(rs.getString("first_name"))
                        .lastName(rs.getString("last_name"))
                        .build())
                .created(rs.getTimestamp("created_at").toInstant())
                .updated(rs.getTimestamp("updated_at").toInstant())
                .postStatus(PostStatus.valueOf(rs.getString("status")))
                .labels(new ArrayList<>())
                .build();
    }
}
