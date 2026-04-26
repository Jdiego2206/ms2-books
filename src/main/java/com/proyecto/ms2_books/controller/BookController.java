package com.proyecto.ms2_books.controller;
import com.proyecto.ms2_books.dto.PagedResponse;
import com.proyecto.ms2_books.model.Book;
import com.proyecto.ms2_books.service.BookService;
import com.proyecto.ms2_books.storage.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Books management")
@SecurityRequirement(name = "bearerAuth")
public class BookController {

    private final BookService bookService;
    private final S3Service s3Service;

    @GetMapping
    @Operation(summary = "Get available books (paginated). Query params: ?category=id&search=title&page=1&size=20")
    public PagedResponse<Book> getAll(
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        if (search != null && !search.isEmpty()) {
            return bookService.search(search, page, size);
        }
        if (category != null) {
            return bookService.getByCategory(category, page, size);
        }
        return bookService.getAll(page, size);
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
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Book existing = bookService.getById(id);
        if (!existing.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(bookService.update(id, book));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete: marks active=false. Only the owner")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload book cover photo. Only the owner. Replaces previous photo.")
    public ResponseEntity<?> uploadPhoto(
            @PathVariable Long id,
            @RequestParam("photo") MultipartFile photo) {
        if (!s3Service.isConfigured()) {
            return ResponseEntity.status(503).body(Map.of("error", "Storage service not configured"));
        }
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Book book = bookService.getById(id);
        if (!book.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Only the owner can upload photos"));
        }
        try {
            return ResponseEntity.ok(bookService.uploadPhoto(id, photo, s3Service));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error uploading photo"));
        }
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