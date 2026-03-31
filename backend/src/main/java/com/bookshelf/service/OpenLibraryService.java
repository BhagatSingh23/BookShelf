package com.bookshelf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OpenLibraryService {

    private static final String ALLOWED_HOST   = "openlibrary.org";
    private static final String ALLOWED_SCHEME = "https";
    private static final String BASE_URL       = ALLOWED_SCHEME + "://" + ALLOWED_HOST;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper  = new ObjectMapper();

    public List<Map<String, Object>> searchBooks(String query) {
        String sanitizedQuery = sanitizeQuery(query);

        URI uri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL + "/search.json")
                .queryParam("q", sanitizedQuery)
                .queryParam("limit", 15)
                .queryParam("fields", "key,title,author_name,cover_i,number_of_pages_median,subject")
                .build()
                .toUri();

        validateUri(uri);

        try {
            String response = restTemplate.getForObject(uri, String.class);
            JsonNode root   = objectMapper.readTree(response);
            JsonNode docs   = root.get("docs");

            List<Map<String, Object>> results = new ArrayList<>();

            if (docs != null && docs.isArray()) {
                for (JsonNode doc : docs) {
                    Map<String, Object> book = new HashMap<>();

                    String key = doc.has("key") ? doc.get("key").asText() : null;
                    book.put("olBookId", key != null ? key.replace("/works/", "") : null);
                    book.put("title",    doc.has("title") ? doc.get("title").asText() : "Unknown");

                    if (doc.has("author_name") && doc.get("author_name").isArray()) {
                        book.put("author", doc.get("author_name").get(0).asText());
                    } else {
                        book.put("author", "Unknown Author");
                    }

                    if (doc.has("cover_i")) {
                        book.put("coverUrl",
                            "https://covers.openlibrary.org/b/id/" + doc.get("cover_i").asText() + "-M.jpg");
                    } else {
                        book.put("coverUrl", null);
                    }

                    book.put("totalPages", doc.has("number_of_pages_median")
                            ? doc.get("number_of_pages_median").asInt() : 0);

                    if (doc.has("subject") && doc.get("subject").isArray()) {
                        book.put("genre", doc.get("subject").get(0).asText());
                    } else {
                        book.put("genre", null);
                    }

                    book.put("olUrl", key != null ? BASE_URL + key : null);
                    results.add(book);
                }
            }

            return results;

        } catch (SecurityException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to search Open Library: " + e.getMessage());
        }
    }

    // Allow only safe search characters — no URLs, IPs, or special chars
    private String sanitizeQuery(String query) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }
        // Single quote appears once only, no duplicate
        String sanitized = query.replaceAll("[^a-zA-Z0-9 '.\\-]", "").trim();
        if (sanitized.isBlank()) {
            throw new IllegalArgumentException("Invalid search query");
        }
        return sanitized;
    }

    // Strict allowlist — only openlibrary.org over https is ever permitted
    private void validateUri(URI uri) {
        if (!ALLOWED_HOST.equals(uri.getHost()) || !ALLOWED_SCHEME.equals(uri.getScheme())) {
            throw new SecurityException(
                "SSRF blocked: request to disallowed destination: " + uri);
        }
    }
}
