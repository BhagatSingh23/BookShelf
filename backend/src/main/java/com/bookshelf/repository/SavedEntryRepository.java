package com.bookshelf.repository;

import com.bookshelf.entity.SavedEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// JpaRepository is a repository interface that provides CRUD operations for entities of type T
// This repository is connected to the SavedEntry table in the database
// This repo will be used to interact with the database
public interface SavedEntryRepository extends JpaRepository<SavedEntry, Long> {
    List<SavedEntry> findAllByBook_IdOrderByCreatedAtDesc(Long bookId);
    List<SavedEntry> findAllByBook_User_IdOrderByCreatedAtDesc(Long userId);
    void deleteByIdAndBook_User_Id(Long entryId, Long userId);
}
