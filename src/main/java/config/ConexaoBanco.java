package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBanco {

    private static final String URL =
            System.getenv("DB_URL");

    private static final String USER =
            System.getenv("DB_USER");

    private static final String PASSW0RD =
            System.getenv("DB_PASSWORD");

    public static Connection USER() throws SQLException {

        if (URL == null || USER == null || PASSW0RD == null) {
            throw new IllegalStateException(
                    "Configure DB_URL, DB_USER e DB_PASSWORD."
            );
        }

        return DriverManager.getConnection(URL, USER, PASSW0RD);
    }
}