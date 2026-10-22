package br.com.myfinance.category;

import br.com.myfinance.exeption.DuplicateResourceException;
import br.com.myfinance.exeption.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    private CategoryRepository repository;
    private CategoryService service;

    @BeforeEach
    void setUp() {
        repository = new CategoryRepositoryFake();
        service = new CategoryService(repository);
    }

    @Nested
    @DisplayName("Testes de Caminho Feliz (Sucesso)")
    class HappyPathTests {

        @Test
        @DisplayName("Deve criar uma categoria com sucesso")
        void shouldCreateCategorySuccessfully() {
            CategoryDTO.Create fakeCategory = CategoryBuilder.newCategory().buildDtoCreate();

            CategoryDTO.Response response = service.create(fakeCategory);

            assertNotNull(response);
            assertEquals(1, response.id());
            assertEquals("Alimentação", response.name());
            assertEquals("Despesas com mercado e restaurantes", response.description());
            assertTrue(response.active());
        }

        @Test
        @DisplayName("Deve encontrar categoria por ID com sucesso")
        void shouldFindCategoryByIdSuccessfully() {
            Category fakeCategory = CategoryBuilder.newCategory().build();
            repository.create(fakeCategory);

            CategoryDTO.Response response = service.findById(1);

            assertNotNull(response);
            assertEquals(1, response.id());
            assertEquals("Alimentação", response.name());
            assertEquals("Despesas com mercado e restaurantes", response.description());
            assertTrue(response.active());
        }

        @Test
        @DisplayName("Deve listar todas as categorias com sucesso")
        void shouldFindAllCategoriesSuccessfully() {
            repository.create(CategoryBuilder.newCategory().build());
            repository.create(CategoryBuilder.newCategory()
                    .withName("Transporte")
                    .withDescription("Despesas com mobilidade")
                    .build());

            List<CategoryDTO.Response> responses = service.findAll();

            assertNotNull(responses);
            assertEquals(2, responses.size());
            assertEquals("Alimentação", responses.get(0).name());
            assertEquals("Despesas com mercado e restaurantes", responses.get(0).description());
            assertTrue(responses.get(0).active());
            assertEquals("Transporte", responses.get(1).name());
            assertEquals("Despesas com mobilidade", responses.get(1).description());
            assertTrue(responses.get(1).active());
        }

        @Test
        @DisplayName("Deve atualizar a categoria com sucesso")
        void shouldUpdateCategorySuccessfully() {
            Category fakeCategory = CategoryBuilder.newCategory().build();
            repository.create(fakeCategory);

            CategoryDTO.Update updatedFakeCategory = CategoryBuilder.newCategory()
                    .withId(fakeCategory.getId())
                    .withName("Transporte")
                    .withDescription("Despesas com mobilidade")
                    .inactive()
                    .buildDtoUpdate();

            CategoryDTO.Response response = service.update(updatedFakeCategory);

            assertNotNull(response);
            assertEquals(1, response.id());
            assertEquals("Transporte", response.name());
            assertEquals("Despesas com mobilidade", response.description());
            assertFalse(response.active());
        }

        @Test
        @DisplayName("Deve deletar a categoria com sucesso")
        void shouldDeleteCategorySuccessfully() {
            Category fakeCategory = CategoryBuilder.newCategory().build();
            repository.create(fakeCategory);

            CategoryDTO.Delete deletedFakeCategory = CategoryBuilder.newCategory()
                    .withId(fakeCategory.getId())
                    .buildDtoDelete();

            CategoryDTO.Response response = service.delete(deletedFakeCategory);

            assertNotNull(response);
            assertEquals(1, response.id());
            assertNotNull(response.deleteAt());
            assertEquals("Alimentação", response.name());
            assertEquals("Despesas com mercado e restaurantes", response.description());
            assertTrue(response.active());
        }


    }

    @Nested
    @DisplayName("Testes de Caminho Triste (Exceções e Erros)")
    class SadPathTests {

        @Test
        @DisplayName("Deve lançar exceção ao tentar criar categoria com nome já existente")
        void shouldThrowExceptionWhenCreatingWithDuplicateName() {
            Category fakeCategory = CategoryBuilder.newCategory().build();
            repository.create(fakeCategory);

            CategoryDTO.Create duplicateCategoryName = CategoryBuilder.newCategory().buildDtoCreate();

            DuplicateResourceException exception = assertThrows(
                    DuplicateResourceException.class,
                    () -> service.create(duplicateCategoryName)
            );

            assertEquals("Já existe uma categoria cadastrada com o nome: Alimentação", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção ao buscar ID inexistente")
        void shouldThrowExceptionWhenIdNotFound() {

            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.findById(99)
            );

            assertEquals("Categoria não encontrada com o id: 99", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar atualizar categoria com um id inexistente")
        void shouldThrowExceptionWhenUpdatingWithIdNotFound() {
            CategoryDTO.Update fakeCategory = CategoryBuilder.newCategory().withId(99).buildDtoUpdate();

            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.update(fakeCategory)
            );

            assertEquals("Categoria não encontrada com o id: 99", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar atualizar categoria com nome já existente")
        void shouldThrowExceptionWhenUpdatingWithDuplicateName() {
            Category fakeCategory = CategoryBuilder.newCategory().build();
            repository.create(fakeCategory);
            Category fakeCategoryTwo = CategoryBuilder.newCategory().withName("Transporte").withDescription("Despesas com mobilidade").build();
            repository.create(fakeCategoryTwo);

            CategoryDTO.Update duplicateCategoryName = CategoryBuilder.newCategory().withId(fakeCategoryTwo.getId()).buildDtoUpdate();

            DuplicateResourceException exception = assertThrows(
                    DuplicateResourceException.class,
                    () -> service.update(duplicateCategoryName)
            );

            assertEquals("Já existe uma categoria cadastrada com o nome: Alimentação", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar deletar categoria com um id inexistente")
        void shouldThrowExceptionWhenDeletingWithIdNotFound() {
            CategoryDTO.Delete fakeCategory = CategoryBuilder.newCategory().withId(99).buildDtoDelete();

            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.delete(fakeCategory)
            );

            assertEquals("Categoria não encontrada com o id: 99", exception.getMessage());
        }
    }
}