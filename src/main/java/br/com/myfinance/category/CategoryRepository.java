package br.com.myfinance.category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category create(Category category);
    Optional<Category> findById(Integer id);
    Optional<Category> findByName(String name);
    List<Category> findAll();
    Category update(Category category);
    Category softDelete(Integer id);
}
