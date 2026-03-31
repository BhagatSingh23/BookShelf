package com.bookshelf.controller;

import com.bookshelf.dto.request.ProgressRequest;
import com.bookshelf.service.ReadingProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books/{bookId}/progress")
@RequiredArgsConstructor
public class ReadingProgressController {

    private final ReadingProgressService progressService;

    // PATCH /api/books/{bookId}/progress
    // Body: { pagesRead, isCompleted }
    @PatchMapping
    public ResponseEntity<?> updateProgress(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable Long bookId,
            @RequestBody ProgressRequest req) {
        return ResponseEntity.ok(
                progressService.updateProgress(user.getUsername(), bookId, req));
    }
}
