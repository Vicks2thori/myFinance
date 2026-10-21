package br.com.myfinance.category;

import br.com.myfinance.exeption.DuplicateResourceException;
import br.com.myfinance.exeption.ResourceNotFoundException;
import java.util.List;

public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public CategoryDTO.Response create(CategoryDTO.Create dto) {

        if (repository.findByName(dto.name())) {
            throw new DuplicateResourceException("Já existe uma categoria cadastrada com o nome: " + dto.name());
        }

        Category category = new Category(
                null,
                dto.name(),
                dto.description(),
                dto.active(),
                null,
                null,
                null
        );

        return convert(repository.create(category));
    }

    public CategoryDTO.Response findById(Integer id) {

        Category category = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoria não encontrada com o id:" + id)
                );

        return convert(category);
    }

    public List<CategoryDTO.Response> findAll() {
        return repository.findAll()
                .stream()
                .map(this::convert)
                .toList();
    }

    public CategoryDTO.Response update(CategoryDTO.Update dto) {

        Category exist = repository.findById(dto.id())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Categoria não encontrada com o id:" + dto.id())
                );

        if (dto.name() != null) {
            if (repository.findByName(dto.name(), dto.id())) {
                throw new DuplicateResourceException("Já existe uma categoria cadastrada com o nome: " + dto.name());
            }

            exist.setName(dto.name());
        }

        if (dto.description() != null) {
            exist.setDescription(dto.description());
        }

        if (dto.active() != null) {
            exist.setActive(dto.active());
        }

        if (dto.id() != null){
            exist.setId(dto.id());
        }

        Category updated = repository.update(exist);
        return convert(updated);
    }

    public CategoryDTO.Response delete(CategoryDTO.Delete dto) {
        return convert(repository.softDelete(dto.id()));
    }

    private CategoryDTO.Response convert(Category category) {
        return new CategoryDTO.Response(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.isActive(),
                category.getCreatedAt(),
                category.getUpdatedAt(),
                category.getDeletedAt()
        );
    }
}