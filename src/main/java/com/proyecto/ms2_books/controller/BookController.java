package com.proyecto.ms2_books.controller;

import com.proyecto.ms2_books.model.Book;
import com.proyecto.ms2_books.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Books management")
@SecurityRequirement(name = "bearerAuth")
public class BookController {

    private final BookService bookService;

    @GetMapping
    @Operation(summary = "Get all available books")
    public List<Book> getAll() {
        return bookService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID")
    public ResponseEntity<Book> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getById(id));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get books by user ID")
    public List<Book> getByUser(@PathVariable Long userId) {
        return bookService.getByUser(userId);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get books by category")
    public List<Book> getByCategory(@PathVariable Long categoryId) {
        return bookService.getByCategory(categoryId);
    }

    @GetMapping("/search")
    @Operation(summary = "Search books by title")
    public List<Book> search(@RequestParam String title) {
        return bookService.search(title);
    }

    @PostMapping
    @Operation(summary = "Create a new book")
    public ResponseEntity<Book> create(@RequestBody Book book) {
        return ResponseEntity.ok(bookService.create(book));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book")
    public ResponseEntity<Book> update(@PathVariable Long id, @RequestBody Book book) {
        return ResponseEntity.ok(bookService.update(id, book));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete a book")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}