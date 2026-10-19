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

    public CategoryDTO buscarPorId(Integer id) {
        validarId(id);

        Category category = repository.buscarPorId(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Categoria não encontrada."
                        )
                );

        return converter(category);
    }

    public List<CategoryDTO> listarTodos() {
        return repository.listarTodos()
                .stream()
                .map(this::converter)
                .toList();
    }

    public CategoryDTO atualizar(Integer id, CategoryDTO dto) {
        validarId(id);
        validar(dto);

        Category existente = repository.buscarPorId(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Categoria não encontrada."
                        )
                );

        existente.setName(dto.name().trim());
        existente.setDescription(dto.description()
        );
        existente.setActive(dto.active());

        return converter(repository.atualizar(existente));
    }

    public boolean excluir(Integer id) {

        return repository.excluirLogicamente(id);
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