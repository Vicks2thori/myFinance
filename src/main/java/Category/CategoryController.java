package Category;

import java.util.List;

public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    public CategoryDTO cadastrar(CategoryDTO dto) {
        return service.cadastrar(dto);
    }

    public CategoryDTO buscarPorId(Integer id) {
        return service.buscarPorId(id);
    }

    public List<CategoryDTO> listarTodos() {
        return service.listarTodos();
    }

    public CategoryDTO atualizar(Integer id, CategoryDTO dto) {
        return service.atualizar(id, dto);
    }

    public boolean excluir(Integer id) {
        return service.excluir(id);
    }
}