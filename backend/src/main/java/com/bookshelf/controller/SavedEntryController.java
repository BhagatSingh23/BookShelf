package com.bookshelf.controller;

import com.bookshelf.dto.request.SavedEntryRequest;
import com.bookshelf.dto.response.SavedEntryResponse;
import com.bookshelf.service.SavedEntryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SavedEntryController {

    private final SavedEntryService entryService;

    // POST /api/books/{bookId}/entries  — add entry to a book
    @PostMapping("/books/{bookId}/entries")
    public ResponseEntity<SavedEntryResponse> addEntry(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long bookId,
            @Valid @RequestBody SavedEntryRequest req) {
        return ResponseEntity.ok(
                entryService.addEntry(user.getUsername(), bookId, req));
    }

    // GET /api/books/{bookId}/entries  — get all entries for a book
    @GetMapping("/books/{bookId}/entries")
    public ResponseEntity<List<SavedEntryResponse>> getEntriesForBook(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long bookId) {
        return ResponseEntity.ok(
                entryService.getEntriesForBook(user.getUsername(), bookId));
    }

    // GET /api/entries  — get ALL entries across all books
    @GetMapping("/entries")
    public ResponseEntity<List<SavedEntryResponse>> getAllEntries(
            @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(entryService.getAllEntries(user.getUsername()));
    }

    // DELETE /api/entries/{entryId}  — delete an entry
    @DeleteMapping("/entries/{entryId}")
    public ResponseEntity<?> deleteEntry(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long entryId) {
        entryService.deleteEntry(user.getUsername(), entryId);
        return ResponseEntity.ok(Map.of("message", "Entry deleted."));
    }
}
