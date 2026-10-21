package br.com.myfinance.category;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    @DisplayName("Testes de Create DTO)")
    class CreatePathsTests {

        @Test
        @DisplayName("Deve invalidar DTO de Create com nome em branco")
        void shouldInvalidateWhenCreateNameIsBlank() {

            CategoryDTO.Create fakeCategoryDTO = CategoryBuilder.newCategory().withName("   ").buildDtoCreate();

            var violations = validator.validate(fakeCategoryDTO);

            assertFalse(violations.isEmpty());
            boolean hasExpectedMessage = violations.stream()
                    .anyMatch(v -> v.getMessage().equals("O nome é obrigatório."));

            assertTrue(hasExpectedMessage, "Deveria conter a mensagem de erro de nome obrigatório.");
        }

        @Test
        @DisplayName("Deve invalidar DTO de Create com nome maior do que 100 caracteres")
        void shouldInvalidateWhenCreateNameIsLengthMoreMax() {

            CategoryDTO.Create fakeCategoryDTO = CategoryBuilder.newCategory().nameIsLong().buildDtoCreate();

            var violations = validator.validate(fakeCategoryDTO);

            assertFalse(violations.isEmpty());
            boolean hasExpectedMessage = violations.stream()
                    .anyMatch(v -> v.getMessage().equals("O nome deve ter de 1 a 100 caracteres."));

            assertTrue(hasExpectedMessage, "Deveria conter a mensagem de erro de nome grande.");
        }

        @Test
        @DisplayName("Deve invalidar DTO de Create com descrição maior do que 255 caracteres")
        void shouldInvalidateWhenCreateDescriptionIsLengthMoreMax() {

            CategoryDTO.Create fakeCategoryDTO = CategoryBuilder.newCategory().withDescription(256).buildDtoCreate();

            var violations = validator.validate(fakeCategoryDTO);

            assertFalse(violations.isEmpty());
            boolean hasExpectedMessage = violations.stream()
                    .anyMatch(v -> v.getMessage().equals("A descrição deve ter no máximo 255 caracteres."));

            assertTrue(hasExpectedMessage, "Deveria conter a mensagem de erro de descrição grande.");
        }

        @Test
        @DisplayName("Deve invalidar DTO de Create com status nulo")
        void shouldInvalidateWhenCreateActiveIsNull() {

            CategoryDTO.Create fakeCategoryDTO = CategoryBuilder.newCategory().withActive(null).buildDtoCreate();

            var violations = validator.validate(fakeCategoryDTO);

            assertFalse(violations.isEmpty());
            boolean hasExpectedMessage = violations.stream()
                    .anyMatch(v -> v.getMessage().equals("O status é obrigatório."));

            assertTrue(hasExpectedMessage, "Deveria conter a mensagem de erro de status obrigatório");
        }

        @Test
        @DisplayName("Deve criar DTO de Create com sucesso quando os dados são válidos")
        void shouldCreateDtoSuccessfullyWhenDataIsValid() {

            CategoryDTO.Create fakeCategoryDTO = CategoryBuilder.newCategory().buildDtoCreate();

            var violations = validator.validate(fakeCategoryDTO);

            assertTrue(violations.isEmpty());
            assertEquals("Alimentação", fakeCategoryDTO.name());
            assertEquals("Despesas com mercado e restaurantes", fakeCategoryDTO.description());
            assertTrue(fakeCategoryDTO.active());
        }
    }

    @Nested
    @DisplayName("Testes de Update DTO)")
    class UpdatePathsTests {

        @Test
        @DisplayName("Deve invalidar DTO de Update com id null")
        void shouldInvalidateWhenUpdateIdIsNull() {

            CategoryDTO.Update fakeCategoryDTO = CategoryBuilder.newCategory().withId(null).buildDtoUpdate();

            var violations = validator.validate(fakeCategoryDTO);

            assertFalse(violations.isEmpty());
            boolean hasExpectedMessage = violations.stream()
                    .anyMatch(v -> v.getMessage().equals("O id é obrigatório."));

            assertTrue(hasExpectedMessage, "Deveria conter a mensagem de erro de id obrigatório.");
        }

        @Test
        @DisplayName("Deve invalidar DTO de Update com id negativo")
        void shouldInvalidateWhenUpdateIdIsNegative() {

            CategoryDTO.Update fakeCategoryDTO = CategoryBuilder.newCategory().withId(-1).buildDtoUpdate();

            var violations = validator.validate(fakeCategoryDTO);

            assertFalse(violations.isEmpty());
            boolean hasExpectedMessage = violations.stream()
                    .anyMatch(v -> v.getMessage().equals("O id deve ser maior que 0."));

            assertTrue(hasExpectedMessage, "Deveria conter a mensagem de erro de id inválido.");
        }

        @Test
        @DisplayName("Deve invalidar DTO de Update com nome maior do que 100 caracteres")
        void shouldInvalidateWhenUpdateNameIsLengthMoreMax() {

            CategoryDTO.Update fakeCategoryDTO = CategoryBuilder.newCategory().nameIsLong().buildDtoUpdate();

            var violations = validator.validate(fakeCategoryDTO);

            assertFalse(violations.isEmpty());
            boolean hasExpectedMessage = violations.stream()
                    .anyMatch(v -> v.getMessage().equals("O nome deve ter de 1 a 100 caracteres."));

            assertTrue(hasExpectedMessage, "Deveria conter a mensagem de erro de nome grande.");
        }

        @Test
        @DisplayName("Deve invalidar DTO de Update com descrição maior do que 255 caracteres")
        void shouldInvalidateWhenUpdateDescriptionIsLengthMoreMax() {

            CategoryDTO.Update fakeCategoryDTO = CategoryBuilder.newCategory().withDescription(256).buildDtoUpdate();

            var violations = validator.validate(fakeCategoryDTO);

            assertFalse(violations.isEmpty());
            boolean hasExpectedMessage = violations.stream()
                    .anyMatch(v -> v.getMessage().equals("A descrição deve ter no máximo 255 caracteres."));

            assertTrue(hasExpectedMessage, "Deveria conter a mensagem de erro de descrição grande.");
        }

        @Test
        @DisplayName("Deve criar DTO de Update com sucesso quando os dados são válidos")
        void shouldUpdateDtoSuccessfullyWhenDataIsValid() {

            CategoryDTO.Update fakeCategoryDTO = CategoryBuilder.newCategory()
                    .withName("Transporte")
                    .withDescription("Despesa com locomoção")
                    .inactive()
                    .buildDtoUpdate();

            var violations = validator.validate(fakeCategoryDTO);

            assertTrue(violations.isEmpty());
            assertEquals("Transporte", fakeCategoryDTO.name());
            assertEquals("Despesa com locomoção", fakeCategoryDTO.description());
            assertFalse(fakeCategoryDTO.active());
        }
    }

    @Nested
    @DisplayName("Testes de Delete DTO)")
    class DeletePathsTests {

        @Test
        @DisplayName("Deve invalidar DTO de Delete com id null")
        void shouldInvalidateWhenDeleteIdIsNull() {

            CategoryDTO.Delete fakeCategoryDTO = CategoryBuilder.newCategory().withId(null).buildDtoDelete();

            var violations = validator.validate(fakeCategoryDTO);

            assertFalse(violations.isEmpty());
            boolean hasExpectedMessage = violations.stream()
                    .anyMatch(v -> v.getMessage().equals("O id é obrigatório."));

            assertTrue(hasExpectedMessage, "Deveria conter a mensagem de erro de id obrigatório.");
        }

        @Test
        @DisplayName("Deve invalidar DTO de Delete com id negativo")
        void shouldInvalidateWhenDeleteIdIsNegative() {

            CategoryDTO.Delete fakeCategoryDTO = CategoryBuilder.newCategory().withId(-1).buildDtoDelete();

            var violations = validator.validate(fakeCategoryDTO);

            assertFalse(violations.isEmpty());
            boolean hasExpectedMessage = violations.stream()
                    .anyMatch(v -> v.getMessage().equals("O id deve ser maior que 0."));

            assertTrue(hasExpectedMessage, "Deveria conter a mensagem de erro de id inválido.");
        }

        @Test
        @DisplayName("Deve criar DTO de Delete com sucesso quando os dados são válidos")
        void shouldDeleteDtoSuccessfullyWhenDataIsValid() {

            CategoryDTO.Delete fakeCategoryDTO = CategoryBuilder.newCategory().withId(5).buildDtoDelete();

            var violations = validator.validate(fakeCategoryDTO);

            assertTrue(violations.isEmpty());
            assertEquals(5, fakeCategoryDTO.id());
        }
    }
}