package Category;

import java.util.List;

public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public CategoryDTO.Response create(CategoryDTO.Create dto) {

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
                        new IllegalArgumentException(
                                "Categoria não encontrada."
                        )
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
                        new IllegalArgumentException(
                                "Categoria não encontrada."
                        )
                );

        if (dto.name() != null) {
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
        return convert(repository.logicalDelete(dto.id()));
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