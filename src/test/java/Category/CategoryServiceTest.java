package Category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

    // Configuração inicial executada ANTES de cada teste
    @BeforeEach
    void setUp() {
        repositoryMock = Mockito.mock(CategoryRepository.class);
        service = new CategoryService(repositoryMock);
    }

    // =========================================================================
    // 🟢 CAMINHOS FELIZES (HAPPY PATHS)
    // =========================================================================

    @Test
    @DisplayName("Caminho Feliz 1: Deve encontrar categoria por ID com sucesso")
    void shouldFindCategoryByIdSuccessfully() {
        // 1. ARRANGE (Preparar)
        Category fakeCategory = new Category(
                1, "Alimentação", "Mercado", true,
                OffsetDateTime.now(), OffsetDateTime.now(), null
        );
        when(repositoryMock.findById(1)).thenReturn(Optional.of(fakeCategory));

        // 2. ACT (Agir)
        CategoryDTO.Response response = service.findById(1);

        // 3. ASSERT (Verificar)
        assertNotNull(response);
        assertEquals(1, response.id());
        assertEquals("Alimentação", response.name());
        verify(repositoryMock, times(1)).findById(1); // Garante que chamou o banco 1 vez
    }

    @Test
    @DisplayName("Caminho Feliz 2: Deve criar uma categoria com sucesso")
    void shouldCreateCategorySuccessfully() {
        // 1. ARRANGE
        CategoryDTO.Create createDTO = new CategoryDTO.Create("Bebidas", "Sucos e refrigerantes", true);
        Category savedCategory = new Category(
                10, "Bebidas", "Sucos e refrigerantes", true,
                OffsetDateTime.now(), OffsetDateTime.now(), null
        );
        when(repositoryMock.create(any(Category.class))).thenReturn(savedCategory);

        // 2. ACT
        CategoryDTO.Response response = service.create(createDTO);

        // 3. ASSERT
        assertNotNull(response);
        assertEquals(10, response.id());
        assertEquals("Bebidas", response.name());
    }

    // =========================================================================
    // 🔴 CAMINHOS TRISTES (SAD PATHS / ERROS)
    // =========================================================================

    @Test
    @DisplayName("Caminho Triste 1: Deve lançar exceção ao buscar ID inexistente")
    void shouldThrowExceptionWhenIdNotFound() {
        // 1. ARRANGE
        when(repositoryMock.findById(99)).thenReturn(Optional.empty());

        // 2. ACT & 3. ASSERT
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.findById(99)
        );

        assertEquals("Categoria não encontrada.", exception.getMessage());
    }

    @Test
    @DisplayName("Caminho Triste 2: Deve lançar exceção ao tentar atualizar categoria inexistente")
    void shouldThrowExceptionWhenUpdatingNonExistingCategory() {
        // 1. ARRANGE
        CategoryDTO.Update updateDTO = new CategoryDTO.Update(99, "Novo Nome", "Desc", true);
        when(repositoryMock.findById(99)).thenReturn(Optional.empty());

        // 2. ACT & 3. ASSERT
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.update(updateDTO)
        );

        assertEquals("Categoria não encontrada.", exception.getMessage());
        // Garante que NUNCA tentou atualizar no banco se não encontrou o ID!
        verify(repositoryMock, never()).update(any());
    }
}