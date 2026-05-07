package com.ascend.backend.service;

import com.ascend.backend.entity.Book;
import com.ascend.backend.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> getBooks(String author, Long pageToken) {
        boolean hasAuthor = author != null && !author.trim().isEmpty();
        boolean hasToken = pageToken != null && pageToken > 0;

        if (hasAuthor && hasToken) {
            return repository.findTop10ByAuthorAndIdGreaterThanOrderByIdAsc(author, pageToken);
        } else if (hasAuthor) {
            return repository.findTop10ByAuthorOrderByIdAsc(author);
        } else if (hasToken) {
            return repository.findTop10ByIdGreaterThanOrderByIdAsc(pageToken);
        } else {
            return repository.findTop10ByOrderByIdAsc();
        }
    }

    public Book save(Book book) {
        if (book.getPublishedDate() != null) {
            int currentBuddhistYear = LocalDate.now().getYear() + 543;
            int requestYear = book.getPublishedDate().getYear();

            if (requestYear <= 1000 || requestYear > currentBuddhistYear) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid publishedDate");
            }

            book.setPublishedDate(book.getPublishedDate().minusYears(543));
        }
        return repository.save(book);
    }
}