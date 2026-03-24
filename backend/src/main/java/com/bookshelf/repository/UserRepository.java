package com.bookshelf.repository;

import com.bookshelf.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// JpaRepository is a repository interface that provides CRUD operations for entities of type T
// This repository is connected to the users table in the database
// This repo will be used to interact with the database
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}

