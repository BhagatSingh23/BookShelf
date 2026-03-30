package com.bookshelf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OpenLibraryService {

    @Value("${openlibrary.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Map<String, Object>> searchBooks(String query) {
        String url = baseUrl + "/search.json?q=" +
                query.replace(" ", "+") + "&limit=15&fields=key,title,author_name,cover_i,number_of_pages_median,subject,first_publish_year";

        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode docs = root.get("docs");

            List<Map<String, Object>> results = new ArrayList<>();

            if (docs != null && docs.isArray()) {
                for (JsonNode doc : docs) {
                    Map<String, Object> book = new HashMap<>();

                    String key = doc.has("key") ? doc.get("key").asText() : null;
                    book.put("olBookId", key != null ? key.replace("/works/", "") : null);
                    book.put("title", doc.has("title") ? doc.get("title").asText() : "Unknown");

                    // Author
                    if (doc.has("author_name") && doc.get("author_name").isArray()) {
                        book.put("author", doc.get("author_name").get(0).asText());
                    } else {
                        book.put("author", "Unknown Author");
                    }

                    // Cover image URL
                    if (doc.has("cover_i")) {
                        String coverId = doc.get("cover_i").asText();
                        book.put("coverUrl", "https://covers.openlibrary.org/b/id/" + coverId + "-M.jpg");
                    } else {
                        book.put("coverUrl", null);
                    }

                    // Page count
                    book.put("totalPages", doc.has("number_of_pages_median")
                            ? doc.get("number_of_pages_median").asInt() : 0);

                    // Genre (first subject)
                    if (doc.has("subject") && doc.get("subject").isArray()) {
                        book.put("genre", doc.get("subject").get(0).asText());
                    } else {
                        book.put("genre", null);
                    }

                    // Direct Open Library link
                    book.put("olUrl", key != null ? "https://openlibrary.org" + key : null);

                    results.add(book);
                }
            }

            return results;

        } catch (Exception e) {
            throw new RuntimeException("Failed to search Open Library: " + e.getMessage());
        }
    }
}
