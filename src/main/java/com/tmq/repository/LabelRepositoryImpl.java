package com.tmq.repository;

import com.tmq.exception.GeneralException;
import com.tmq.exception.LabelNotFoundException;
import com.tmq.model.Label;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tmq.sql.LabelRepositorySql.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LabelRepositoryImpl implements LabelRepository {

    private final static LabelRepository INSTANCE = new LabelRepositoryImpl();

    public static LabelRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Label> findAll(Connection connection) {
        try {
            List<Label> labels = new ArrayList<>();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL);
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
    public Optional<Label> findById(Long id, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL);
        preparedStatement.setLong(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            Long resultId = resultSet.getLong("id");
            String name = resultSet.getString("name");
            return Optional.of(new Label(resultId, name));
        }
        throw new LabelNotFoundException();
    }

    @Override
    public Optional<Label> findByName(String name, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_NAME);
        preparedStatement.setString(1, name);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return Optional.of(new Label(
                    resultSet.getLong("id"),
                    resultSet.getString("name")
            ));
        }
        throw new LabelNotFoundException();
    }

    @Override
    public List<Label> findByPostId(Long postId, Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_POST_ID);
        preparedStatement.setLong(1, postId);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Label> labels = new ArrayList<>();
        while (resultSet.next()) {
            labels.add(findById(resultSet.getLong("label_id"), connection).orElseThrow(GeneralException::new));
        }

        return labels;
    }

    @Override
    public Label save(Label label, Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, label.getName());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            label.setId(resultSet.getLong("id"));
            return label;
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public boolean saveLabelToPost(Long labelId, Long postId, Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_LABEL_TO_POST);
            preparedStatement.setLong(1, postId);
            preparedStatement.setLong(2, labelId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public Label update(Label label, Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, label.getName());
            preparedStatement.setLong(2, label.getId());
            int i = preparedStatement.executeUpdate();

            if (i > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                generatedKeys.next();
                return new Label(generatedKeys.getLong("id"), label.getName());
            } else {
                throw new LabelNotFoundException();
            }
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public boolean delete(Long id, Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, id);
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
