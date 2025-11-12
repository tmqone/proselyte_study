package com.tmq.repository;

import com.tmq.model.User;

import java.util.Optional;

public interface UserRepository extends GenericRepository<Integer, User> {
    Optional<User> findByUsername(String username);
}
