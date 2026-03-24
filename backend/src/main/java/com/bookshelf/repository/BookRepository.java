package com.bookshelf.repository;

import com.bookshelf.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

// JpaRepository is a repository interface that provides CRUD operations for entities of type T
// This repository is connected to the Book table in the database
// This repo will be used to interact with the database
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByUser_IdOrderByAddedAtDesc(Long userId);
    Optional<Book> findByIdAndUser_Id(Long bookId, Long userId);
    void deleteByIdAndUser_Id(Long bookId, Long userId);
}
