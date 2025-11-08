package com.tmq.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GenericRepository<ID, T>{
    List<T> findAll();
    Optional<T> findById(ID id);
    T save(T t);
    T update(T t);
    boolean delete(ID id);
    boolean existsById(ID id);
}