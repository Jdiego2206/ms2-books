package com.proyecto.ms2_books.repository;

import com.proyecto.ms2_books.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByActiveTrueAndAvailableTrue();
    List<Book> findByUserId(Long userId);
    List<Book> findByCategoryIdAndActiveTrue(Long categoryId);
    List<Book> findByTitleContainingIgnoreCaseAndActiveTrue(String title);
}
