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

    @GetMapping("/buyer/{buyerId}")
    @Operation(summary = "Get transactions by buyer")
    public List<Transaction> getByBuyer(@PathVariable Long buyerId) {
        return transactionService.getByBuyer(buyerId);
    }

    @GetMapping("/seller/{sellerId}")
    @Operation(summary = "Get transactions by seller")
    public List<Transaction> getBySeller(@PathVariable Long sellerId) {
        return transactionService.getBySeller(sellerId);
    }

    @PostMapping
    @Operation(summary = "Create a new transaction")
    public ResponseEntity<Transaction> create(@RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.create(transaction));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update transaction status")
    public ResponseEntity<Transaction> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(transactionService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete a transaction")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}