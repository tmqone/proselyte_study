package com.tmq.repository;

import com.tmq.model.Writer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface WriterRepository extends GenericRepository<Long, Writer>{
    List<Writer> findByName(String firstName, String lastName, Connection connection);
}
