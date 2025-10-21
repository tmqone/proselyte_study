package com.tmq.repository;

import com.tmq.exception.GeneralException;
import com.tmq.exception.WriterNotFoundException;
import com.tmq.model.Writer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tmq.sql.WriterRepositorySql.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WriterRepositoryImpl implements WriterRepository {
    private final static WriterRepository INSTANCE = new WriterRepositoryImpl();

    public static WriterRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Writer> findAll(Connection connection) {
        try {
            ResultSet resultSet = connection.prepareStatement(FIND_ALL_SQL).executeQuery();
            List<Writer> writers = new ArrayList<>();
            while (resultSet.next()) {
                writers.add(Writer.builder()
                        .id(resultSet.getLong("id"))
                        .firstName(resultSet.getString("first_name"))
                        .lastName(resultSet.getString("last_name"))
                        .build());
            }
            return writers;
        } catch (SQLException e){
            throw new GeneralException(e);
        }
    }

    @Override
    public Optional<Writer> findById(Long id, Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(Writer.builder()
                        .id(id)
                        .lastName(resultSet.getString("last_name"))
                        .firstName(resultSet.getString("first_name"))
                        .build());
            }
            return Optional.empty();
        } catch (SQLException e){
            throw new GeneralException(e);
        }
    }

    @Override
    public List<Writer> findByName(String firstName, String lastName, Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_NAME_SQL);
            preparedStatement.setString(1, "%".concat(firstName).concat("%"));
            preparedStatement.setString(2, "%".concat(lastName).concat("%"));
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Writer> writers = new ArrayList<>();
            while (resultSet.next()) {
                writers.add(
                        new Writer(
                                resultSet.getLong("id"),
                                resultSet.getString("first_name"),
                                resultSet.getString("last_name"),
                                null
                        )
                );
            }
            if (writers.isEmpty()) throw new WriterNotFoundException();
            return writers;
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public Writer save(Writer writer, Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, writer.getFirstName());
            preparedStatement.setString(2, writer.getLastName());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                writer.setId(resultSet.getLong(1));
            }
            return writer;
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public Writer update(Writer writer, Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL);
            preparedStatement.setString(1, writer.getFirstName());
            preparedStatement.setString(2, writer.getLastName());
            preparedStatement.setLong(3, writer.getId());
            preparedStatement.executeUpdate();
            return writer;
        } catch (SQLException e){
            throw new GeneralException(e);
        }
    }

    @Override
    public boolean delete(Long id, Connection connection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_ID_SQL);
            preparedStatement.setLong(1, id);
            int i = preparedStatement.executeUpdate();
            return i > 0;
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }
}
