package Category;

import java.util.List;

public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public CategoryDTO cadastrar(CategoryDTO dto) {
        validar(dto);

        Category category = new Category(
                null,
                dto.name().trim(),
                normalizarDescricao(dto.description()),
                true,
                null,
                null,
                null
        );

        return converter(repository.salvar(category));
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
        existente.setDescription(
                normalizarDescricao(dto.description())
        );
        existente.setActive(dto.active());

        return converter(repository.atualizar(existente));
    }

    public boolean excluir(Integer id) {
        validarId(id);

        return repository.excluirLogicamente(id);
    }

    private void validar(CategoryDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException(
                    "Os dados são obrigatórios."
            );
        }

        if (dto.name() == null || dto.name().isBlank()) {
            throw new IllegalArgumentException(
                    "O nome é obrigatório."
            );
        }

        if (dto.name().trim().length() > 100) {
            throw new IllegalArgumentException(
                    "O nome deve ter no máximo 100 caracteres."
            );
        }

        if (dto.description() != null
                && dto.description().length() > 255) {
            throw new IllegalArgumentException(
                    "A descrição deve ter no máximo 255 caracteres."
            );
        }
    }

    private String normalizarDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            return null;
        }
        return descricao.trim();
    }

    private void validarId(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "O ID deve ser um número positivo."
            );
        }
    }

    private CategoryDTO converter(Category category) {
        return new CategoryDTO(
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