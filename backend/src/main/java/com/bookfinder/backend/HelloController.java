package com.bookfinder.backend;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.core.ParameterizedTypeReference;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class HelloController {

    private final RestClient restClient = RestClient.create();

    // Fetches books from Open Library and then converts them into
    // book objects, it then returns a list of book objects.
    @GetMapping("/api/books")
    public List<Book> searchBooks(@RequestParam String query) {

        String url = "https://openlibrary.org/search.json?q="
                + URLEncoder.encode(query, StandardCharsets.UTF_8)
                + "&limit=10";

        Map<String, Object> data = restClient.get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});

        List<Book> books = new ArrayList<>();

        if (data == null || data.get("docs") == null) {
            return books;
        }

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> docs =
                (List<Map<String, Object>>) data.get("docs");

        for (Map<String, Object> record : docs) {

            String title = (String) record.getOrDefault("title", "Unknown title");
            String id = (String) record.getOrDefault("key", "");

            String author = "Unknown author";

            Object authorData = record.get("author_name");

            if (authorData instanceof List<?> authors && !authors.isEmpty()) {
                author = String.valueOf(authors.get(0));
            }

            String description = "No description available";

            String thumbnail = "";

            Object coverId = record.get("cover_i");

            if (coverId != null) {
                thumbnail = "https://covers.openlibrary.org/b/id/"
                        + coverId + "-M.jpg";
            }

            Book book = new Book(
                    title,
                    author,
                    description,
                    thumbnail,
                    id
            );

            books.add(book);
        }

        return books;
    }
}
