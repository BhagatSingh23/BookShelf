package com.bookshelf.repository;

import com.bookshelf.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;

// JpaRepository is a repository interface that provides CRUD operations for entities of type T
// This repository is connected to the UserSession table in the database
// This repo will be used to interact with the database
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    Optional<UserSession> findByTokenHash(String tokenHash);
    void deleteAllByExpiresAtBefore(LocalDateTime now);
    void deleteAllByUser_Id(Long userId);
}
