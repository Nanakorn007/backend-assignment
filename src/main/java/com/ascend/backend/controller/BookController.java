package com.ascend.backend.controller;

import com.ascend.backend.entity.Book;
import com.ascend.backend.service.BookService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping
    public Map<String, Object> getBooks(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String pageToken
    ) {
        Long parsedToken = null;
        if (pageToken != null && !pageToken.trim().isEmpty()) {
            try {
                byte[] decodedBytes = Base64.getDecoder().decode(pageToken);
                parsedToken = Long.parseLong(new String(decodedBytes, StandardCharsets.UTF_8));
            } catch (Exception e) {
                parsedToken = null;
            }
        }

        List<Book> books = service.getBooks(author, parsedToken);

        Map<String, Object> response = new HashMap<>();
        response.put("data", books);

        if (!books.isEmpty()) {
            Long nextId = books.getLast().getId();
            String nextToken = Base64.getEncoder().encodeToString(String.valueOf(nextId).getBytes(StandardCharsets.UTF_8));
            response.put("nextPageToken", nextToken);
        } else {
            response.put("nextPageToken", null);
        }

        return response;
    }

    @PostMapping
    public Book create(@Valid @RequestBody Book book) {
        return service.save(book);
    }
}