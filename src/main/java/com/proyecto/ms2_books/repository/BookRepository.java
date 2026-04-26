package com.proyecto.ms2_books.repository;

import com.proyecto.ms2_books.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByActiveTrueAndAvailableTrue(Pageable pageable);
    List<Book> findByUserId(Long userId);
    Page<Book> findByCategory_IdAndActiveTrueAndAvailableTrue(Long categoryId, Pageable pageable);
    Page<Book> findByTitleContainingIgnoreCaseAndActiveTrue(String title, Pageable pageable);
}
