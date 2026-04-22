package com.proyecto.ms2_books;

import com.proyecto.ms2_books.model.Category;
import com.proyecto.ms2_books.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

    private final CategoryRepository categoryRepository;

    private static final List<String> CATEGORIES = List.of(
            "Ficción", "Ciencia", "Historia", "Filosofía",
            "Terror", "Romance", "Biografía", "Tecnología", "Arte", "Poesía"
    );

    @Override
    public void run(ApplicationArguments args) {
        if (categoryRepository.count() == 0) {
            CATEGORIES.forEach(name -> {
                Category cat = new Category();
                cat.setName(name);
                cat.setDescription("Categoría: " + name);
                categoryRepository.save(cat);
            });
            System.out.println("Seeded " + CATEGORIES.size() + " categories");
        }
    }
}
