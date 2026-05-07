package com.ascend.backend.controller;

import com.ascend.backend.entity.Book;
import com.ascend.backend.repository.BookRepository;
import tools.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository repository;

    @Autowired
    private JsonMapper jsonMapper;  // เปลี่ยนจาก ObjectMapper เป็น JsonMapper (Jackson 3)

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void shouldSaveValidBookWithBuddhistYear() throws Exception {
        Book book = new Book();
        book.setTitle("Harry Potter");
        book.setAuthor("J.K. Rowling");
        book.setPublishedDate(LocalDate.of(2540, 6, 26));

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.publishedDate").value("1997-06-26"));
    }

    @Test
    void shouldReturnBadRequestWhenTitleIsEmpty() throws Exception {
        Book book = new Book();
        book.setTitle("");
        book.setAuthor("J.K. Rowling");
        book.setPublishedDate(LocalDate.of(2540, 6, 26));

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(book)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenPublishedDateIsFuture() throws Exception {
        Book book = new Book();
        book.setTitle("Future Book");
        book.setAuthor("Author");
        book.setPublishedDate(LocalDate.of(3000, 1, 1));

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(book)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetBooksAndReturnNextPageToken() throws Exception {
        for (int i = 1; i <= 15; i++) {
            Book book = new Book();
            book.setTitle("Book " + i);
            book.setAuthor("Author X");
            book.setPublishedDate(LocalDate.of(2020, 1, 1));
            repository.save(book);
        }

        mockMvc.perform(get("/books?author=Author X"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(10))
                .andExpect(jsonPath("$.nextPageToken").isNotEmpty());
    }
}