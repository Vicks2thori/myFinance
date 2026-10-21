package br.com.myfinance.category;

import br.com.myfinance.exeption.DuplicateResourceException;
import br.com.myfinance.exeption.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    private CategoryRepository repositoryMock;
    private CategoryService service;

    @BeforeEach
    void setUp() {
        repositoryMock = Mockito.mock(CategoryRepository.class);
        service = new CategoryService(repositoryMock);
    }

    @Nested
    @DisplayName("Testes de Caminho Feliz (Sucesso)")
    class HappyPathTests {

        @Test
        @DisplayName("Deve encontrar categoria por ID com sucesso")
        void shouldFindCategoryByIdSuccessfully() {

            Category fakeCategory = CategoryBuilder.newCategory().build();

            when(repositoryMock.findById(1)).thenReturn(Optional.of(fakeCategory));

            CategoryDTO.Response response = service.findById(1);

            assertNotNull(response);
            assertEquals(1, response.id());
            assertEquals("Alimentação", response.name());
            verify(repositoryMock, times(1)).findById(1);
        }

        @Test
        @DisplayName("Deve criar uma categoria com sucesso")
        void shouldCreateCategorySuccessfully() {
            CategoryDTO.Create createDTO = new CategoryDTO.Create("Bebidas", "Sucos e refrigerantes", true);

            Category savedCategory = CategoryBuilder.newCategory()
                    .withId(10)
                    .withName("Bebidas")
                    .build();

            when(repositoryMock.findByName("Bebidas")).thenReturn(false);
            when(repositoryMock.create(any(Category.class))).thenReturn(savedCategory);

            CategoryDTO.Response response = service.create(createDTO);

            assertNotNull(response);
            assertEquals(10, response.id());
            assertEquals("Bebidas", response.name());
            verify(repositoryMock, times(1)).create(any(Category.class));
        }
    }

    @Nested
    @DisplayName("Testes de Caminho Triste (Exceções e Erros)")
    class SadPathTests {

        @Test
        @DisplayName("Deve lançar exceção ao buscar ID inexistente")
        void shouldThrowExceptionWhenIdNotFound() {
            when(repositoryMock.findById(99)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> service.findById(99)
            );

            assertEquals("Categoria não encontrada com o id:99", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar criar categoria com nome já existente")
        void shouldThrowExceptionWhenCreatingWithDuplicateName() {
            CategoryDTO.Create createDTO = new CategoryDTO.Create("Alimentação", "Desc", true);

            // Simula que a busca por nome encontrou que JÁ EXISTE essa categoria no banco
            when(repositoryMock.findByName("Alimentação")).thenReturn(true);

            DuplicateResourceException exception = assertThrows(
                    DuplicateResourceException.class,
                    () -> service.create(createDTO)
            );

            assertEquals("Já existe uma categoria cadastrada com o nome: Alimentação", exception.getMessage());
            verify(repositoryMock, never()).create(any());
        }
    }
}