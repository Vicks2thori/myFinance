package Category;

import config.ConexaoBanco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryRepository {

    public Category create(Category category) {
        String sql = """
                INSERT INTO category (name, description)
                VALUES (?, ?)
                RETURNING id, name, description, is_active,
                          created_at, updated_at, deleted_at
                """;

        try (Connection conn = ConexaoBanco.USER();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }

            throw new SQLException("O banco não retornou a categoria.");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar categoria.", e);
        }
    }

    public Optional<Category> buscarPorId(Integer id) {
        String sql = """
                SELECT id, name, description, is_active,
                       created_at, updated_at, deleted_at
                FROM category
                WHERE id = ? AND deleted_at IS NULL
                """;

        try (Connection conn = ConexaoBanco.USER();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar categoria.", e);
        }
    }

    public List<Category> listarTodos() {
        String sql = """
                SELECT id, name, description, is_active,
                       created_at, updated_at, deleted_at
                FROM category
                WHERE deleted_at IS NULL
                ORDER BY name
                """;

        List<Category> categorias = new ArrayList<>();

        try (Connection conn = ConexaoBanco.USER();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                categorias.add(mapear(rs));
            }

            return categorias;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar categorias.", e);
        }
    }

    public Category atualizar(Category category) {
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

        try (Connection conn = ConexaoBanco.USER();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getName());
            stmt.setString(2, category.getDescription());
            stmt.setBoolean(3, category.isActive());
            stmt.setInt(4, category.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
                throw new IllegalArgumentException(
                        "Categoria não encontrada ou já excluída."
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar categoria.", e);
        }
    }

    public boolean excluirLogicamente(Integer id) {
        String sql = """
                UPDATE category
                SET is_active = FALSE,
                    deleted_at = CURRENT_TIMESTAMP,
                    updated_at = CURRENT_TIMESTAMP
                WHERE id = ? AND deleted_at IS NULL
                """;

        try (Connection conn = ConexaoBanco.USER();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir categoria.", e);
        }
    }

    private Category mapear(ResultSet rs) throws SQLException {
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