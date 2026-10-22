package br.com.myfinance.category;

import br.com.myfinance.exeption.DuplicateResourceException;
import br.com.myfinance.exeption.ResourceNotFoundException;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class CategoryRepositoryFake implements CategoryRepository {

    private final Map<Integer, Category> database = new HashMap<>();
    private final AtomicInteger sequence = new AtomicInteger(1);

    @Override
    public Category create(Category category) {
        if (findByName(category.getName())) {
            throw new DuplicateResourceException("Já existe uma categoria cadastrada com este nome.");
        }

        Integer generatedId = sequence.getAndIncrement();
        category.setId(generatedId);

        database.put(generatedId, category);
        return category;
    }

    @Override
    public Optional<Category> findById(Integer id) {
        Category category = database.get(id);
        return Optional.ofNullable(category);
    }

    @Override
    public boolean findByName(String name) {
        return database.values().stream()
                .anyMatch(category -> category.getName().equalsIgnoreCase(name));
    }

    @Override
    public boolean findByName(String name, Integer id) {
        return database.values().stream()
                .anyMatch(category ->
                        category.getName().equalsIgnoreCase(name) && !category.getId().equals(id)
                );
    }

    @Override
    public List<Category> findAll() {
        return database.values().stream().toList();
    }

    @Override
    public Category update(Category category) {
        if (findByName(category.getName(), category.getId())) {
            throw new DuplicateResourceException("Já existe uma categoria cadastrada com este nome.");
        }

        database.replace(category.getId(), category);
        return database.get(category.getId());
    }

    @Override
    public Category softDelete(Integer id) {
        Category category = findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com o ID: " + id));

        category.setDeletedAt(OffsetDateTime.now());
        return category;
    }
}
