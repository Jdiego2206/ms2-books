package com.proyecto.ms2_books.service;

import com.proyecto.ms2_books.model.Book;
import com.proyecto.ms2_books.model.Transaction;
import com.proyecto.ms2_books.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BookService bookService;

    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    public Transaction getById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found: " + id));
    }

    public List<Transaction> getByBuyer(Long buyerId) {
        return transactionRepository.findByBuyerId(buyerId);
    }

    public List<Transaction> getBySeller(Long sellerId) {
        return transactionRepository.findBySellerId(sellerId);
    }

    public Transaction create(Transaction transaction) {
        Book book = bookService.getById(transaction.getBook().getId());
        transaction.setBook(book);
        transaction.setSellerId(book.getUserId());
        return transactionRepository.save(transaction);
    }

    public Transaction updateStatus(Long id, String status) {
        Transaction t = getById(id);
        t.setStatus(status);
        t.setUpdatedAt(LocalDateTime.now());
        return transactionRepository.save(t);
    }

    public void delete(Long id) {
        Transaction t = getById(id);
        t.setActive(false);
        t.setUpdatedAt(LocalDateTime.now());
        transactionRepository.save(t);
    }
}