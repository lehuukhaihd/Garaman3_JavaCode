package com.garaman3.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBContext {

    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/GaraMan3DB?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Ho_Chi_Minh";

    private DBContext() {
    }

    public static Connection getConnection() throws SQLException {
        String url = setting("GARAMAN3_DB_URL", DEFAULT_URL);
        String username = setting("GARAMAN3_DB_USER", "root");
        String password = setting("GARAMAN3_DB_PASSWORD", "");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            throw new SQLException("MySQL Connector/J is missing from the application.", ex);
        }
        return DriverManager.getConnection(url, username, password);
    }

    private static String setting(String name, String defaultValue) {
        String value = System.getProperty(name);
        if (value == null || value.trim().isEmpty()) {
            value = System.getenv(name);
        }
        return value == null || value.trim().isEmpty() ? defaultValue : value;
    }
}
