package com.bookshelf.service;

import com.bookshelf.dto.request.BookRequest;
import com.bookshelf.dto.response.BookResponse;
import com.bookshelf.entity.Book;
import com.bookshelf.entity.ReadingProgress;
import com.bookshelf.entity.User;
import com.bookshelf.repository.BookRepository;
import com.bookshelf.repository.ReadingProgressRepository;
import com.bookshelf.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final ReadingProgressRepository progressRepository;
    private final UserRepository userRepository;

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public BookResponse addBook(String email, BookRequest req) {
        User user = getUser(email);

        // Prevent duplicate books
        if (req.getOlBookId() != null && !req.getOlBookId().isBlank()) {
            if (bookRepository.existsByUser_IdAndOlBookId(user.getId(), req.getOlBookId())) {
                throw new RuntimeException("DUPLICATE: This book is already in your shelf");
            }
        } else {
            if (bookRepository.existsByUser_IdAndTitleIgnoreCase(user.getId(), req.getTitle())) {
                throw new RuntimeException("DUPLICATE: This book is already in your shelf");
            }
        }

        Book book = Book.builder()
                .user(user)
                .title(req.getTitle())
                .author(req.getAuthor())
                .genre(req.getGenre())
                .totalPages(req.getTotalPages() != null ? req.getTotalPages() : 0)
                .coverUrl(req.getCoverUrl())
                .olBookId(req.getOlBookId())
                .olUrl(req.getOlUrl())
                .build();

        book = bookRepository.save(book);

        ReadingProgress progress = ReadingProgress.builder()
                .book(book)
                .pagesRead(0)
                .isCompleted(false)
                .build();
        progressRepository.save(progress);

        return toResponse(book, progress);
    }

    public List<BookResponse> getBooks(String email) {
        User user = getUser(email);
        return bookRepository.findAllByUser_IdOrderByAddedAtDesc(user.getId())
                .stream()
                .map(book -> {
                    ReadingProgress p = progressRepository
                            .findByBook_Id(book.getId())
                            .orElse(null);
                    return toResponse(book, p);
                })
                .collect(Collectors.toList());
    }

    public BookResponse getBook(String email, Long bookId) {
        User user = getUser(email);
        Book book = bookRepository.findByIdAndUser_Id(bookId, user.getId())
                .orElseThrow(() -> new RuntimeException("Book not found"));
        ReadingProgress p = progressRepository.findByBook_Id(bookId).orElse(null);
        return toResponse(book, p);
    }

    @Transactional
    public void deleteBook(String email, Long bookId) {
        User user = getUser(email);
        Book book = bookRepository.findByIdAndUser_Id(bookId, user.getId())
                .orElseThrow(() -> new RuntimeException("Book not found"));
        bookRepository.delete(book);
    }

    private BookResponse toResponse(Book book, ReadingProgress p) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .genre(book.getGenre())
                .totalPages(book.getTotalPages())
                .coverUrl(book.getCoverUrl())
                .olBookId(book.getOlBookId())
                .olUrl(book.getOlUrl())
                .addedAt(book.getAddedAt())
                .pagesRead(p != null ? p.getPagesRead() : 0)
                .isCompleted(p != null ? p.getIsCompleted() : false)
                .build();
    }
}
