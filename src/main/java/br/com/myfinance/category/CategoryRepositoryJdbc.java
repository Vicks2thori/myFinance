package br.com.myfinance.category;

import br.com.myfinance.config.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryRepositoryJdbc implements CategoryRepository {

    public Category create(Category category) {
        String sql = """
                INSERT INTO category (name, description, is_active)
                VALUES (?, ?, ?)
                RETURNING id, name, description, is_active,
                          created_at, updated_at, deleted_at
                """;

        try (Connection conn = DatabaseConnection.USER();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setBoolean(3, category.isActive());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }

            throw new SQLException("O banco não retornou a categoria.");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar categoria.", e);
        }
    }

    public Optional<Category> findById(Integer id) {
        String sql = """
                SELECT id, name, description, is_active,
                       created_at, updated_at, deleted_at
                FROM category
                WHERE id = ? AND deleted_at IS NULL
                """;

        try (Connection conn = DatabaseConnection.USER();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar categoria.", e);
        }
    }

    public Optional<Category> findByName(String name) {
    String sql = """
            SELECT id, name, description, is_active,
                   created_at, updated_at, deleted_at 
            FROM category
            WHERE LOWER(name) = LOWER(?) AND deleted_at IS NULL
            """;

    try (Connection conn = DatabaseConnection.USER();
    PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, name.trim());

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return Optional.of(map(rs));
            }
            return Optional.empty();
        }

    } catch (SQLException e) {
        throw new RuntimeException("Erro ao buscar categoria por nome.", e);
    }
}

    public List<Category> findAll() {
        String sql = """
                SELECT id, name, description, is_active,
                       created_at, updated_at, deleted_at
                FROM category
                WHERE deleted_at IS NULL
                ORDER BY name
                """;

        List<Category> categories = new ArrayList<>();

        try (Connection conn = DatabaseConnection.USER();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categories.add(map(rs));
            }

            return categories;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar categorias.", e);
        }
    }

    public Category update(Category category) {
        String sql = """
                UPDATE category
                SET name = ?,
                    description = ?,
                    is_active = ?,
                    updated_at = CURRENT_TIMESTAMP
                WHERE id = ? AND deleted_at IS NULL
                RETURNING id, name, description, is_active,
                          created_at, updated_at, deleted_at
                """;

        try (Connection conn = DatabaseConnection.USER();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setBoolean(3, category.isActive());
            stmt.setInt(4, category.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
                throw new IllegalArgumentException(
                        "Categoria não encontrada ou já excluída."
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar categoria.", e);
        }
    }

    public Category softDelete(Integer id) {
        String sql = """
                UPDATE category
                SET is_active = FALSE,
                    deleted_at = CURRENT_TIMESTAMP,
                    updated_at = CURRENT_TIMESTAMP
                WHERE id = ? AND deleted_at IS NULL
                RETURNING id, name, description, is_active,
                          created_at, updated_at, deleted_at
                """;

        try (Connection conn = DatabaseConnection.USER();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
                throw new IllegalArgumentException(
                        "Categoria não encontrada ou já excluída."
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir categoria.", e);
        }
    }

    private Category map(ResultSet rs) throws SQLException {
        return new Category(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getBoolean("is_active"),
                rs.getObject("created_at",
                        java.time.OffsetDateTime.class),
                rs.getObject("updated_at",
                        java.time.OffsetDateTime.class),
                rs.getObject("deleted_at",
                        java.time.OffsetDateTime.class)
        );
    }
}