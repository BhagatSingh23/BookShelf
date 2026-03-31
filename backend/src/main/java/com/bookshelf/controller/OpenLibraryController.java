package com.bookshelf.controller;

import com.bookshelf.service.OpenLibraryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class OpenLibraryController {

    private final OpenLibraryService openLibraryService;

    // GET /api/search?q=atomic+habits
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> search(
            @RequestParam String q) {
        return ResponseEntity.ok(openLibraryService.searchBooks(q));
    }
}
