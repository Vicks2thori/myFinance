package br.com.myfinance.category;

import java.time.OffsetDateTime;

public class CategoryBuilder {

    private Integer id = 1;
    private String name = "Alimentação";
    private String description = "Despesas com mercado e restaurantes";
    private Boolean active = true;
    private final OffsetDateTime createdAt = OffsetDateTime.now();
    private final OffsetDateTime updatedAt = OffsetDateTime.now();
    private OffsetDateTime deletedAt = null;

    private CategoryBuilder() {}

    public static CategoryBuilder newCategory() {
        return new CategoryBuilder();
    }

    public CategoryBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public CategoryBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CategoryBuilder withName(Integer size) {
        this.name = longString(size);
        return this;
    }

    public CategoryBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public CategoryBuilder withDescription(Integer size) {
        this.description = longString(size);
        return this;
    }

    public CategoryBuilder withActive(Boolean active){
        this.active = active;
        return this;
    }

    public CategoryBuilder inactive() {
        this.active = false;
        return this;
    }

    public CategoryBuilder deleted(){
        this.deletedAt = OffsetDateTime.now();
        return this;
    }

    public CategoryBuilder nameIsLong(){
        this.name = longString(101);
        return this;
    }

    private String longString(Integer size) {
        return "A".repeat(size);
    }

    public Category build() {
        return new Category(id, name, description, active, createdAt, updatedAt, deletedAt);
    }

    public CategoryDTO.Create buildDtoCreate() {
        return new CategoryDTO.Create(name, description, active);
    }

    public CategoryDTO.Update buildDtoUpdate() {
        return new CategoryDTO.Update(id, name, description, active);
    }

    public CategoryDTO.Delete buildDtoDelete() {
        return new CategoryDTO.Delete(id);
    }
}