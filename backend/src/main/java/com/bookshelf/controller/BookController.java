package com.bookshelf.controller;

import com.bookshelf.dto.request.BookRequest;
import com.bookshelf.dto.response.BookResponse;
import com.bookshelf.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // GET /api/books  — get all books for logged-in user
    @GetMapping
    public ResponseEntity<List<BookResponse>> getBooks(
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(bookService.getBooks(user.getUsername()));
    }

    // GET /api/books/{id}  — get one book
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBook(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBook(user.getUsername(), id));
    }

    // POST /api/books  — add a book to shelf
    @PostMapping
    public ResponseEntity<BookResponse> addBook(
            @AuthenticationPrincipal UserDetails user,
            @Valid @RequestBody BookRequest req) {
        return ResponseEntity.ok(bookService.addBook(user.getUsername(), req));
    }

    // DELETE /api/books/{id}  — remove a book
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long id) {
        bookService.deleteBook(user.getUsername(), id);
        return ResponseEntity.ok(Map.of("message", "Book removed."));
    }
}
