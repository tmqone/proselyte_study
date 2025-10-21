package com.tmq.service;

import com.tmq.exception.GeneralException;
import com.tmq.exception.LabelNotFoundException;
import com.tmq.model.Label;
import com.tmq.repository.LabelRepository;
import com.tmq.repository.LabelRepositoryImpl;
import com.tmq.util.DatabaseUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {
    private final LabelRepository labelRepository;

    @Override
    public List<Label> getAll() {
        try (Connection connection = DatabaseUtil.getConnection()) {
            return labelRepository.findAll(connection);
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public Label getByName(String name) {
        try (Connection connection = DatabaseUtil.getConnection()){
            return labelRepository.findByName(name, connection)
                    .orElseThrow(LabelNotFoundException::new);
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public Label getById(Long id) {
        try (Connection connection = DatabaseUtil.getConnection()){
            return labelRepository.findById(id, connection)
                    .orElseThrow(LabelNotFoundException::new);
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public Label save(Label label) {
        try (Connection connection = DatabaseUtil.getConnection()) {
            return labelRepository.save(label, connection);
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public Label update(Label label) {
        try (Connection connection = DatabaseUtil.getConnection()){
            return labelRepository.update(label, connection);
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public boolean delete(Long id) {
        try (Connection connection = DatabaseUtil.getConnection()){
            return labelRepository.delete(id, connection);
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }
}
