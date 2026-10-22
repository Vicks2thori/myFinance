package br.com.myfinance.category;

import java.util.List;

public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    public CategoryDTO.Response create(CategoryDTO.Create dto) {
        return service.create(dto);
    }

    public CategoryDTO.Response findByID(Integer id) {
        return service.findById(id);
    }

    public List<CategoryDTO.Response> findAll() {
        return service.findAll();
    }

    public CategoryDTO.Response update(CategoryDTO.Update dto) {
        return service.update(dto);
    }

    public CategoryDTO.Response delete(CategoryDTO.Delete dto) {
        return service.delete(dto);
    }
}