package com.proyecto.ms2_books.controller;

import com.proyecto.ms2_books.model.Book;
import com.proyecto.ms2_books.model.Category;
import com.proyecto.ms2_books.service.BookService;
import com.proyecto.ms2_books.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
@Tag(name = "Export", description = "Data export for Analytics/Ingestion")
public class ExportController {

    private final BookService bookService;
    private final CategoryService categoryService;

    @Value("${export.api.key}")
    private String apiKey;

    private boolean isValidKey(String key) {
        return apiKey.equals(key);
    }

    @GetMapping("/books")
    @Operation(summary = "Export all books (including inactive/unavailable)")
    @Parameter(name = "X-API-KEY", in = ParameterIn.HEADER, required = true, schema = @Schema(type = "string"))
    public ResponseEntity<List<Book>> exportBooks(@RequestHeader(value = "X-API-KEY", required = false) String key) {
        if (!isValidKey(key)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return ResponseEntity.ok(bookService.getAllForExport());
    }

    @GetMapping("/categories")
    @Operation(summary = "Export all categories (including inactive)")
    @Parameter(name = "X-API-KEY", in = ParameterIn.HEADER, required = true, schema = @Schema(type = "string"))
    public ResponseEntity<List<Category>> exportCategories(@RequestHeader(value = "X-API-KEY", required = false) String key) {
        if (!isValidKey(key)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return ResponseEntity.ok(categoryService.getAllForExport());
    }
}
