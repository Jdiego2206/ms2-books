package com.proyecto.ms2_books.service;

import com.proyecto.ms2_books.model.Book;
import com.proyecto.ms2_books.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> getAll() {
        return bookRepository.findByActiveTrueAndAvailableTrue();
    }

    public Book getById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found: " + id));
    }

    public List<Book> getByUser(Long userId) {
        return bookRepository.findByUserId(userId);
    }

    public List<Book> getByCategory(Long categoryId) {
        return bookRepository.findByCategory_IdAndActiveTrue(categoryId);
    }

    public List<Book> search(String title) {
        return bookRepository.findByTitleContainingIgnoreCaseAndActiveTrue(title);
    }

    public Book create(Book book) {
        return bookRepository.save(book);
    }

    public Book update(Long id, Book data) {
        Book book = getById(id);
        book.setTitle(data.getTitle());
        book.setAuthor(data.getAuthor());
        book.setDescription(data.getDescription());
        book.setPhotoUrl(data.getPhotoUrl());
        book.setPrice(data.getPrice());
        book.setAvailable(data.getAvailable());
        book.setCategory(data.getCategory());
        return bookRepository.save(book);
    }

    public Book updateAvailability(Long id, Boolean available) {
        Book book = getById(id);
        book.setAvailable(available);
        return bookRepository.save(book);
    }

    public void delete(Long id) {
        Book book = getById(id);
        book.setActive(false);
        bookRepository.save(book);
    }
}