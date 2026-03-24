package com.bookshelf.repository;

import com.bookshelf.entity.ReadingProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// JpaRepository is a repository interface that provides CRUD operations for entities of type T
// This repository is connected to the ReadingProgress table in the database
// This repo will be used to interact with the database
public interface ReadingProgressRepository extends JpaRepository<ReadingProgress, Long> {
    Optional<ReadingProgress> findByBook_Id(Long bookId);
}
