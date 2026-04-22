package com.proyecto.ms2_books.controller;
import com.proyecto.ms2_books.model.Book;
import com.proyecto.ms2_books.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Operation(summary = "Get all available books. Query params: ?category=id&search=title")
    public List<Book> getAll(
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            return bookService.search(search);
        }
        if (category != null) {
            return bookService.getByCategory(category);
        }
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

    @PostMapping
    @Operation(summary = "Publish a book. Body: title, author, category_id, description, photo_url, price (optional)")
    public ResponseEntity<Book> create(@RequestBody Book book) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        book.setUserId(userId);
        return ResponseEntity.ok(bookService.create(book));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edit book. Only the owner")
    public ResponseEntity<Book> update(@PathVariable Long id, @RequestBody Book book) {
        return ResponseEntity.ok(bookService.update(id, book));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete: marks active=false. Only the owner")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    record AvailabilityRequest(Boolean available) {}

    @PutMapping("/{id}/availability")
    @Operation(summary = "Change book availability. Called by MS4")
    public ResponseEntity<Book> updateAvailability(
            @PathVariable Long id,
            @RequestBody AvailabilityRequest body) {
        return ResponseEntity.ok(bookService.updateAvailability(id, body.available()));
    }
}