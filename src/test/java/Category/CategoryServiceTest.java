package Category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.OffsetDateTime;
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
            Category fakeCategory = new Category(
                    1, "Alimentação", "Mercado", true,
                    OffsetDateTime.now(), OffsetDateTime.now(), null
            );
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
            Category savedCategory = new Category(
                    10, "Bebidas", "Sucos e refrigerantes", true,
                    OffsetDateTime.now(), OffsetDateTime.now(), null
            );
            when(repositoryMock.create(any(Category.class))).thenReturn(savedCategory);

            CategoryDTO.Response response = service.create(createDTO);

            assertNotNull(response);
            assertEquals(10, response.id());
            assertEquals("Bebidas", response.name());
        }
    }

    @Nested
    @DisplayName("Testes de Caminho Triste (Exceções e Erros)")
    class SadPathTests {

        @Test
        @DisplayName("Deve lançar exceção ao buscar ID inexistente")
        void shouldThrowExceptionWhenIdNotFound() {
            when(repositoryMock.findById(99)).thenReturn(Optional.empty());

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> service.findById(99)
            );

            assertEquals("Categoria não encontrada.", exception.getMessage());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar atualizar categoria inexistente")
        void shouldThrowExceptionWhenUpdatingNonExistingCategory() {
            CategoryDTO.Update updateDTO = new CategoryDTO.Update(99, "Novo Nome", "Desc", true);
            when(repositoryMock.findById(99)).thenReturn(Optional.empty());

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> service.update(updateDTO)
            );

            assertEquals("Categoria não encontrada.", exception.getMessage());
            verify(repositoryMock, never()).update(any());
        }
    }
}