package com.tmq.repository;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T, ID>{
    List<T> findAll();
    Optional<T> findById(ID id);
    T save(T t); //return object
    T update(T t); //return object
    boolean delete(ID id); //input id
}
