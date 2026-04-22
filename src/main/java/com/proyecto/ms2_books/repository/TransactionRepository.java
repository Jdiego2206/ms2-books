package com.proyecto.ms2_books.repository;

import com.proyecto.ms2_books.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByBuyerId(Long buyerId);
    List<Transaction> findBySellerId(Long sellerId);
    List<Transaction> findByBookId(Long bookId);
    List<Transaction> findByActiveTrueAndStatus(String status);
}