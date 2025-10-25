package com.tmq.repository.jdbc;

import com.tmq.exception.GeneralException;
import com.tmq.exception.WriterNotFoundException;
import com.tmq.model.Writer;
import com.tmq.repository.WriterRepository;
import com.tmq.util.DatabaseUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tmq.sql.WriterRepositorySql.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JdbcWriterRepositoryImpl implements WriterRepository {
    private final static WriterRepository INSTANCE = new JdbcWriterRepositoryImpl();
    public static WriterRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Writer> findAll() {
        try (PreparedStatement preparedStatement = DatabaseUtil.getStatement(FIND_ALL_SQL)){
            ResultSet resultSet = preparedStatement.executeQuery();
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
    public Optional<Writer> findById(Long id) {
        try (PreparedStatement preparedStatement = DatabaseUtil.getStatement(FIND_BY_ID_SQL)){
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(Writer.builder()
                        .id(id)
                        .lastName(resultSet.getString("last_name"))
                        .firstName(resultSet.getString("first_name"))
                        .build());
            }
            throw new WriterNotFoundException();
        } catch (SQLException e){
            throw new GeneralException(e);
        }
    }

    @Override
    public List<Writer> findByName(String firstName, String lastName) {
        try (PreparedStatement preparedStatement = DatabaseUtil.getStatement(FIND_BY_NAME_SQL)){
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
            return writers;
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public Writer save(Writer writer) {
        try (PreparedStatement preparedStatement = DatabaseUtil.getStatementWithGeneratedKeys(SAVE_SQL)){
            preparedStatement.setString(1, writer.getFirstName());
            preparedStatement.setString(2, writer.getLastName());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                writer.setId(resultSet.getLong(1));
            }
            preparedStatement.getConnection().commit();
            return writer;
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public Writer update(Writer writer) {
        try (PreparedStatement preparedStatement = DatabaseUtil.getStatement(UPDATE_SQL)){
            preparedStatement.setString(1, writer.getFirstName());
            preparedStatement.setString(2, writer.getLastName());
            preparedStatement.setLong(3, writer.getId());
            int i = preparedStatement.executeUpdate();
            preparedStatement.getConnection().commit();
            if (i > 0) {
                return writer;
            }
            throw new WriterNotFoundException();
        } catch (SQLException e){
            throw new GeneralException(e);
        }
    }

    @Override
    public boolean delete(Long id) {
        try (PreparedStatement preparedStatement = DatabaseUtil.getStatement(DELETE_BY_ID_SQL)){
            preparedStatement.setLong(1, id);
            int i = preparedStatement.executeUpdate();
            preparedStatement.getConnection().commit();
            return i > 0;
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }
}
