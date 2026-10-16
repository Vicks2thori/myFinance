package Category;

import java.time.OffsetDateTime;

public final class CategoryDTO {

    private CategoryDTO() {
    }

    public record Create(
            String name,
            String description
    ) {}

    public record Response(
            Integer id,
            String name,
            String description,
            boolean active,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {}

    public record Update(
            String name,
            String description,
            Boolean active
    ) {}

    public record Delete(
            Integer id
    ) {}
}
