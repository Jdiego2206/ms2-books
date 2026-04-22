package com.proyecto.ms2_books.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "categories")
@Schema(description = "Categorías de libros (ej: Ficción, Terror, Ciencia)")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único de la categoría", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Nombre de la categoría", example = "Ficción")
    private String name;

    @Schema(description = "Descripción breve", example = "Libros de fantasía, novelas, etc.")
    private String description;

    @Column(name = "created_at", updatable = false)
    @Schema(description = "Fecha de creación", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "active")
    @Schema(description = "Soft delete", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean active = true;

    @JsonIgnore
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Book> books;
}