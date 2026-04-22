package com.proyecto.ms2_books.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "books")
@Schema(description = "Entidad que representa un libro en el sistema")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del libro", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "ID del usuario dueño del libro (se obtiene del token automáticamente)", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
    private Long userId;

    @Column(nullable = false)
    @Schema(description = "Título del libro", example = "El Psicoanalista")
    private String title;

    @Column(nullable = false)
    @Schema(description = "Autor del libro", example = "John Katzenbach")
    private String author;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @Schema(description = "Categoría del libro (enviar solo el ID)", example = "2")
    private Category category;

    @JsonProperty("category_id")
    public void setCategoryId(Long categoryId) {
        if (categoryId != null) {
            this.category = new Category();
            this.category.setId(categoryId);
        }
    }

    @Schema(description = "Descripción o sinopsis del libro", example = "Un thriller psicológico fascinante...")
    private String description;

    @Column(name = "photo_url")
    @Schema(description = "URL de la imagen de portada", example = "https://images.com/book1.jpg")
    private String photoUrl;

    @Schema(description = "Precio del libro (null si es para intercambio)", example = "45.50")
    private BigDecimal price;

    @Column(name = "available")
    @Schema(description = "Indica si el libro está disponible para intercambio/venta", example = "true")
    private Boolean available = true;

    @Column(name = "active")
    @Schema(description = "Soft delete: indica si la publicación sigue activa", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean active = true;

    @Column(name = "created_at", updatable = false)
    @Schema(description = "Fecha de creación del registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt = LocalDateTime.now();

    @JsonIgnore
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<Transaction> transactions;
}