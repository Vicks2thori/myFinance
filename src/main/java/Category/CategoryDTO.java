package Category;

import java.time.OffsetDateTime;

public record CategoryDTO(
        Integer id,
        String name,
        String description,
        boolean active,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        OffsetDateTime deletedAt
) {
}