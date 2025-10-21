package com.tmq.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GenericRepository<ID, T>{
    List<T> findAll(Connection connection) throws SQLException;
    Optional<T> findById(ID id, Connection connection) throws SQLException;
    T save(T t, Connection connection) throws SQLException;
    T update(T t, Connection connection) throws SQLException;
    boolean delete(ID id, Connection connection) throws SQLException;
}