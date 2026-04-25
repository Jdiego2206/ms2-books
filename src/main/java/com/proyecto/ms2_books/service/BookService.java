package com.proyecto.ms2_books.service;

import com.proyecto.ms2_books.dto.PagedResponse;
import com.proyecto.ms2_books.model.Book;
import com.proyecto.ms2_books.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public PagedResponse<Book> getAll(int page, int size) {
        Page<Book> result = bookRepository.findByActiveTrueAndAvailableTrue(PageRequest.of(page - 1, size));
        return toResponse(result, page, size);
    }

    public List<Book> getAllForExport() {
        return bookRepository.findAll();
    }

    public Book getById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found: " + id));
    }

    public List<Book> getByUser(Long userId) {
        return bookRepository.findByUserId(userId);
    }

    public PagedResponse<Book> getByCategory(Long categoryId, int page, int size) {
        Page<Book> result = bookRepository.findByCategory_IdAndActiveTrue(categoryId, PageRequest.of(page - 1, size));
        return toResponse(result, page, size);
    }

    public PagedResponse<Book> search(String title, int page, int size) {
        Page<Book> result = bookRepository.findByTitleContainingIgnoreCaseAndActiveTrue(title, PageRequest.of(page - 1, size));
        return toResponse(result, page, size);
    }

    private PagedResponse<Book> toResponse(Page<Book> p, int page, int size) {
        return new PagedResponse<>(p.getContent(), p.getTotalElements(), page, size, p.getTotalPages());
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