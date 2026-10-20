package br.com.myfinance.category;

import java.time.OffsetDateTime;

public final class CategoryDTO {

    private CategoryDTO() {
    }

    //Validações dos atributos (conforme o banco de dados
    private static boolean isValidId(Integer id){
        return id != null && id > 0;
    }

    private static boolean isValidName(String name){
        return name != null && !name.isBlank()
               && name.length() <= 100;
    }

    private static boolean isValidDescription(String description){
        return description == null
                || description.trim().length() <= 255;
    }

    public record Create(
            String name,
            String description,
            boolean active
    ) {
        public Create {
            if (!isValidName(name)) {
                throw new IllegalArgumentException(
                        "O nome é obrigatório, não pode estar em branco e deve ter no máximo 100 caracteres."
                );
            }

            if (!isValidDescription(description)) {
                throw new IllegalArgumentException(
                        "Descrição deve ter no máximo 255 caracteres."
                );
            }

            name = name.trim();
            description = (description != null) ? description.trim() : null;
        }
    }

    public record Response(
            Integer id,
            String name,
            String description,
            boolean active,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt,
            OffsetDateTime deleteAt
    ) {}

    public record Update(
            Integer id,
            String name,
            String description,
            Boolean active
    ) {
        public Update {
            if (!isValidId(id)) {
                throw new IllegalArgumentException(
                        "O ID é obrigatório e deve ser maior que zero."
                );
            }

            if (name != null && !isValidName(name)) {
                throw new IllegalArgumentException(
                        "O nome não pode estar em branco e deve ter no máximo 100 caracteres."
                );
            }

            if (description != null && !isValidDescription(description)) {
                throw new IllegalArgumentException(
                        "Descrição deve ter no máximo 255 caracteres."
                );
            }

            name = (name != null) ? name.trim() : null;
            description = (description != null) ? description.trim() : null;
        }
    }

    public record Delete(
            Integer id
    ) {
        public Delete {
            if (!isValidId(id)) {
                throw new IllegalArgumentException(
                        "O ID é obrigatório e deve ser maior que zero."
                );
            }
        }
    }
}