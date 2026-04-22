package com.proyecto.ms2_books.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transactions")
@Schema(description = "Entidad que registra las transacciones finalizadas entre usuarios")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la transacción", example = "500", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    @Schema(description = "Libro involucrado en la transacción (enviar solo el ID)", example = "1")
    private Book book;

    @JsonProperty("book_id")
    public void setBookId(Long bookId) {
        this.book = new Book();
        this.book.setId(bookId);
    }

    @Column(name = "buyer_id", nullable = false)
    @Schema(description = "ID del usuario comprador", example = "10")
    private Long buyerId;

    @Column(name = "seller_id", nullable = false)
    @Schema(description = "ID del usuario vendedor", example = "5")
    private Long sellerId;

    @Column(name = "created_at", updatable = false)
    @Schema(description = "Fecha en que se registró la transacción", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt = LocalDateTime.now();
}