package br.com.myfinance.bank;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

public final class BankDTO {

    private BankDTO() {
    }

    public record Create(
            @NotNull(message = "O nome é obrigatório.")
            @Size(min = 1, max = 100, message = "O nome deve ter de 1 a 100 caracteres.")
            String name,

            @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
            String description,

            @NotNull(message = "O status é obrigatório.")
            Boolean active
    ) {
        public Create {
            name = (name != null && !name.isBlank()) ? name.trim() : null;
            description = (description != null && !description.isBlank()) ? description.trim() : null;
        }
    }

    public record Response(
            Integer id,
            String name,
            String description,
            boolean active,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt,
            OffsetDateTime deletedAt
    ) {}

    public record Update(
            @NotNull(message = "O id é obrigatório.")
            @Positive(message = "O id deve ser maior que 0.")
            Integer id,

            @Size(max = 100, message = "O nome deve ter de 1 a 100 caracteres.")
            String name,

            @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
            String description,

            Boolean active
    ) {
        public Update {
            name = (name != null && !name.isBlank()) ? name.trim() : null;
            description = (description != null && !description.isBlank()) ? description.trim() : null;
        }
    }

    public record Delete(
            @NotNull(message = "O id é obrigatório.")
            @Positive(message = "O id deve ser maior que 0.")
            Integer id
    ) {}
}