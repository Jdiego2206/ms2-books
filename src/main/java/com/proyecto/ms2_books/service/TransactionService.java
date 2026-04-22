package com.proyecto.ms2_books.service;

import com.proyecto.ms2_books.model.Book;
import com.proyecto.ms2_books.model.Transaction;
import com.proyecto.ms2_books.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    public List<Transaction> getByUser(Long userId) {
        return transactionRepository.findByBuyerIdOrSellerId(userId, userId);
    }

    public Transaction create(Transaction transaction) {
        Book book = bookService.getById(transaction.getBook().getId());
        transaction.setBook(book);
        transaction.setSellerId(book.getUserId());
        return transactionRepository.save(transaction);
    }
}