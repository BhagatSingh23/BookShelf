package com.bookshelf.repository;

import com.bookshelf.entity.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;

// JpaRepository is a repository interface that provides CRUD operations for entities of type T
// This repository is connected to the OtpCode table in the database
// This repo will be used to interact with the database
public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {
    Optional<OtpCode> findTopByEmailAndUsedFalseAndExpiresAtAfterOrderByCreatedAtDesc(
            String email, LocalDateTime now);
    void deleteAllByExpiresAtBefore(LocalDateTime now);
}
