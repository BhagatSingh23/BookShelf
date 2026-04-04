package com.bookshelf.repository;

import com.bookshelf.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByUser_IdOrderByAddedAtDesc(Long userId);
    Optional<Book> findByIdAndUser_Id(Long bookId, Long userId);
    boolean existsByUser_IdAndOlBookId(Long userId, String olBookId);
    boolean existsByUser_IdAndTitleIgnoreCase(Long userId, String title);
}
