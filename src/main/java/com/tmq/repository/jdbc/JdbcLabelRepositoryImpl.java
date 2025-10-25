package com.tmq.repository.jdbc;

import com.tmq.exception.GeneralException;
import com.tmq.exception.LabelNotFoundException;
import com.tmq.model.Label;
import com.tmq.repository.LabelRepository;
import com.tmq.util.DatabaseUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tmq.sql.LabelRepositorySql.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JdbcLabelRepositoryImpl implements LabelRepository {

    private final static LabelRepository INSTANCE = new JdbcLabelRepositoryImpl();

    public static LabelRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Label> findAll() {
        try (PreparedStatement preparedStatement = DatabaseUtil.getStatement(FIND_ALL_SQL)){
            List<Label> labels = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                labels.add(new Label(id, name));
            }
            return labels;
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public Optional<Label> findById(Long id) {
        try (PreparedStatement preparedStatement = DatabaseUtil.getStatement(FIND_BY_ID_SQL)){
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Long resultId = resultSet.getLong("id");
                String name = resultSet.getString("name");
                return Optional.of(new Label(resultId, name));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new LabelNotFoundException();
    }

    @Override
    public Optional<Label> findByName(String name){
        try (PreparedStatement preparedStatement = DatabaseUtil.getStatement(FIND_BY_NAME)){
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Label(
                        resultSet.getLong("id"),
                        resultSet.getString("name")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new LabelNotFoundException();
    }

    @Override
    public List<Label> findByPostId(Long postId) {
        try (PreparedStatement preparedStatement = DatabaseUtil.getStatement(FIND_BY_POST_ID)){
            preparedStatement.setLong(1, postId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Label> labels = new ArrayList<>();
            while (resultSet.next()) {
                labels.add(findById(resultSet.getLong("label_id")).orElseThrow(GeneralException::new));
            }
            return labels;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Label save(Label label) {
        try (PreparedStatement preparedStatement = DatabaseUtil.getStatementWithGeneratedKeys(SAVE_LABEL_SQL)){
            preparedStatement.setString(1, label.getName());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            label.setId(resultSet.getLong("id"));
            preparedStatement.getConnection().commit();
            return label;
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public boolean saveLabelToPost(Long labelId, Long postId) {
        try (PreparedStatement preparedStatement = DatabaseUtil.getStatement(SAVE_LABEL_TO_POST)){
            preparedStatement.setLong(1, postId);
            preparedStatement.setLong(2, labelId);
            preparedStatement.getConnection().commit();
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public Label update(Label label) {
        try (PreparedStatement preparedStatement = DatabaseUtil.getStatement(UPDATE_SQL);){
            preparedStatement.setString(1, label.getName());
            preparedStatement.setLong(2, label.getId());
            int i = preparedStatement.executeUpdate();
            preparedStatement.getConnection().commit();
            if (i > 0) return label;
            throw new LabelNotFoundException();
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public boolean delete(Long id) {
        try (PreparedStatement preparedStatement = DatabaseUtil.getStatement(DELETE_SQL)){
            preparedStatement.setLong(1, id);
            preparedStatement.getConnection().commit();
            int i = preparedStatement.executeUpdate();
            if (i > 0) {
                return true;
            } else {
                throw new LabelNotFoundException();
            }
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }
}
