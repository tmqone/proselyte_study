package com.tmq.repository;

import com.tmq.model.Label;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface LabelRepository extends GenericRepository<Long, Label>{
    Optional<Label> findByName(String name, Connection connection) throws SQLException;
    List<Label> findByPostId(Long postId, Connection connection) throws SQLException;
    boolean saveLabelToPost(Long labelId, Long postId, Connection connection);
}
