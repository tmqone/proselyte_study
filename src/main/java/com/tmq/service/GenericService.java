package com.tmq.service;

import java.sql.Connection;
import java.util.List;

public interface GenericService<T> {
    List<T> getAll() ;
}
