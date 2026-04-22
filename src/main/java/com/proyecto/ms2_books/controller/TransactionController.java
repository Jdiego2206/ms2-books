package com.proyecto.ms2_books.controller;

import com.proyecto.ms2_books.model.Transaction;
import com.proyecto.ms2_books.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Transactions management")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    @Operation(summary = "Get all transactions")
    public List<Transaction> getAll() {
        return transactionService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID")
    public ResponseEntity<Transaction> getById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getById(id));
    }

    // Este es el que pide el .md: /user/:userId (buyer O seller)
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get all transactions of a user (as buyer or seller)")
    public List<Transaction> getByUser(@PathVariable Long userId) {
        return transactionService.getByUser(userId);
    }

    @PostMapping
    @Operation(summary = "Register a completed transaction. Called by MS4")
    public ResponseEntity<Transaction> create(@RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.create(transaction));
    }
}