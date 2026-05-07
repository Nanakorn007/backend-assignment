package com.ascend.backend.repository;

import com.ascend.backend.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findTop10ByOrderByIdAsc();

    List<Book> findTop10ByIdGreaterThanOrderByIdAsc(Long id);

    List<Book> findTop10ByAuthorOrderByIdAsc(String author);

    List<Book> findTop10ByAuthorAndIdGreaterThanOrderByIdAsc(String author, Long id);
}