package br.com.myfinance.bank;

import br.com.myfinance.category.Category;
import br.com.myfinance.category.CategoryRepository;
import br.com.myfinance.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BankRepositoryJdbc implements BankRepository {

    public Bank create(Bank bank) {
        String sql = """
                INSERT INTO bank (name, description, is_active)
                VALUES (?, ?, ?)
                RETURNING id, name, description, is_active,
                          created_at, updated_at, deleted_at
                """;

        try (Connection conn = DatabaseConnection.USER();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, bank.getName());
            stmt.setString(2, bank.getDescription());
            stmt.setBoolean(3, bank.isActive());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }

            throw new SQLException("O banco não retornou o banco.");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao cadastrar banco.", e);
        }
    }

    public Optional<Bank> findById(Integer id) {
        String sql = """
                SELECT id, name, description, is_active,
                       created_at, updated_at, deleted_at
                FROM bank
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
            throw new RuntimeException("Erro ao buscar banco.", e);
        }
    }

    public boolean findByName(String name) {
    String sql = """
            SELECT 1 
            FROM bank
            WHERE LOWER(name) = LOWER(?) AND deleted_at IS NULL
            """;

    try (Connection conn = DatabaseConnection.USER();
    PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, name.trim());

        try (ResultSet rs = stmt.executeQuery()) {
            return rs.next();
        }

    } catch (SQLException e) {
        throw new RuntimeException("Erro ao buscar banco por nome.", e);
    }
}

    public boolean findByName(String name, Integer id) {
        String sql = """
            SELECT 1 
            FROM bank
            WHERE LOWER(name) = LOWER(?) AND id <> (?) AND deleted_at IS NULL
            """;

        try (Connection conn = DatabaseConnection.USER();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name.trim());
            stmt.setInt(2, id);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar banco por nome.", e);
        }
    }

    public List<Bank> findAll() {
        String sql = """
                SELECT id, name, description, is_active,
                       created_at, updated_at, deleted_at
                FROM bank
                WHERE deleted_at IS NULL
                ORDER BY name
                """;

        List<Bank> banks = new ArrayList<>();

        try (Connection conn = DatabaseConnection.USER();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                banks.add(map(rs));
            }

            return banks;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar bancos.", e);
        }
    }

    public Bank update(Bank bank) {
        String sql = """
                UPDATE bank
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

            stmt.setString(1, bank.getName());
            stmt.setString(2, bank.getDescription());
            stmt.setBoolean(3, bank.isActive());
            stmt.setInt(4, bank.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
                throw new IllegalArgumentException(
                        "Banco não encontrado ou já excluído."
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar banco.", e);
        }
    }

    public Bank softDelete(Integer id) {
        String sql = """
                UPDATE bank
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
                        "Banco não encontrado ou já excluído."
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir banco.", e);
        }
    }

    private Bank map(ResultSet rs) throws SQLException {
        return new Bank(
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