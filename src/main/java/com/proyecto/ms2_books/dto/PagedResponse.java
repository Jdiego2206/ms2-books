package com.proyecto.ms2_books.dto;

import java.util.List;

public record PagedResponse<T>(
    List<T> data,
    long total,
    int page,
    int size,
    int totalPages
) {}
