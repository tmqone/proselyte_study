package com.tmq.repository;

import com.tmq.model.File;

import java.util.List;

public interface FileRepository extends GenericRepository<Integer, File> {
    List<File> findAllByUserId(Integer userId);
    List<File> findByUserId(Integer userId);
}
