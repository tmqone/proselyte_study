package com.tmq.repository;

import com.tmq.model.File;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends GenericRepository<Integer, File> {
    List<File> findAll(Integer userId);
    Optional<File> findById(Integer id, Integer userId);
    boolean delete(Integer id, Integer userId);
}
