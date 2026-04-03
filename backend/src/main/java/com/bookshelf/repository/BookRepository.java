package com.bookshelf.repository;

import com.bookshelf.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByUser_IdOrderByAddedAtDesc(Long userId);
    Optional<Book> findByIdAndUser_Id(Long bookId, Long userId);
    boolean existsByUser_IdAndOlBookId(Long userId, String olBookId);
    boolean existsByUser_IdAndTitleIgnoreCase(Long userId, String title);

    @Modifying
    @Transactional
    @Query("DELETE FROM Book b WHERE b.id = :bookId AND b.user.id = :userId")
    void deleteByIdAndUser_Id(Long bookId, Long userId);
}
