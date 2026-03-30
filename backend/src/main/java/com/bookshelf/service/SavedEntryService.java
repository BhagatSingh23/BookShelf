package com.bookshelf.service;

import com.bookshelf.dto.request.SavedEntryRequest;
import com.bookshelf.dto.response.SavedEntryResponse;
import com.bookshelf.entity.Book;
import com.bookshelf.entity.SavedEntry;
import com.bookshelf.entity.User;
import com.bookshelf.repository.BookRepository;
import com.bookshelf.repository.SavedEntryRepository;
import com.bookshelf.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SavedEntryService {

    private final SavedEntryRepository entryRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    // ── Add entry to a book ──────────────────────────────────
    @Transactional
    public SavedEntryResponse addEntry(String email, Long bookId, SavedEntryRequest req) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findByIdAndUser_Id(bookId, user.getId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        SavedEntry entry = SavedEntry.builder()
                .book(book)
                .entryType(req.getEntryType().toUpperCase())
                .content(req.getContent())
                .pageRef(req.getPageRef())
                .build();

        return toResponse(entryRepository.save(entry));
    }

    // ── Get all entries for one book ─────────────────────────
    public List<SavedEntryResponse> getEntriesForBook(String email, Long bookId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        bookRepository.findByIdAndUser_Id(bookId, user.getId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        return entryRepository.findAllByBook_IdOrderByCreatedAtDesc(bookId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ── Get ALL entries across all books (for AllEntries page) ─
    public List<SavedEntryResponse> getAllEntries(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return entryRepository.findAllByBook_User_IdOrderByCreatedAtDesc(user.getId())
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ── Delete an entry ──────────────────────────────────────
    @Transactional
    public void deleteEntry(String email, Long entryId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        entryRepository.deleteByIdAndBook_User_Id(entryId, user.getId());
    }

    // ── Helper ───────────────────────────────────────────────
    private SavedEntryResponse toResponse(SavedEntry e) {
        return SavedEntryResponse.builder()
                .id(e.getId())
                .bookId(e.getBook().getId())
                .bookTitle(e.getBook().getTitle())
                .entryType(e.getEntryType())
                .content(e.getContent())
                .pageRef(e.getPageRef())
                .createdAt(e.getCreatedAt())
                .build();
    }
}
