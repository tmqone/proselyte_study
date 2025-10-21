package com.tmq.service;

import com.tmq.exception.GeneralException;
import com.tmq.exception.WriterNotFoundException;
import com.tmq.model.Writer;
import com.tmq.repository.WriterRepository;
import com.tmq.repository.WriterRepositoryImpl;
import com.tmq.util.DatabaseUtil;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
public class WriterServiceImpl implements WriterService {
    private final WriterRepository writerRepository;

    @Override
    public List<Writer> getAll() {
        try (var con = DatabaseUtil.getConnection()) {
            return writerRepository.findAll(con);
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public List<Writer> getByName(String firstName, String lastName) {
        try (var con = DatabaseUtil.getConnection()) {
            return writerRepository.findByName(firstName, lastName, con);
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public Writer getById(Long id) {
        try (var con = DatabaseUtil.getConnection()) {
            return writerRepository.findById(id, con)
                    .orElseThrow(WriterNotFoundException::new);
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public Writer save(Writer writer) {
        try (var con = DatabaseUtil.getConnection()) {
            return writerRepository.save(writer, con);
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public Writer update(Writer writer) {
        try (var con = DatabaseUtil.getConnection()) {
            return writerRepository.update(writer, con);
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }

    @Override
    public boolean delete(Long id) {
        try (var con = DatabaseUtil.getConnection()) {
            return writerRepository.delete(id, con);
        } catch (SQLException e) {
            throw new GeneralException(e);
        }
    }
}
