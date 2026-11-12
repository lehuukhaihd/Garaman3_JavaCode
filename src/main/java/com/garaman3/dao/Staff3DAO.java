package com.garaman3.dao;

import com.garaman3.entity.StaffAccount;
import com.garaman3.util.PasswordHasher;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Staff3DAO {

    private final Connection connection;

    public Staff3DAO(Connection connection) {
        this.connection = connection;
    }

    public StaffAccount authenticate(String username, String password) throws SQLException {
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            return null;
        }

        String id;
        String storedUsername;
        String storedPassword;
        String fullName;
        String role;
        String sql = "SELECT id, username, password, fullName, role FROM tblStaff3 WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username.trim());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }
                id = resultSet.getString("id");
                storedUsername = resultSet.getString("username");
                storedPassword = resultSet.getString("password");
                fullName = resultSet.getString("fullName");
                role = resultSet.getString("role");
                if (resultSet.next()) {
                    return null;
                }
            }
        }

        boolean encoded = PasswordHasher.isEncoded(storedPassword);
        try {
            boolean passwordMatches = encoded
                    ? PasswordHasher.matches(password, storedPassword)
                    : PasswordHasher.matchesLegacyPassword(password, storedPassword);
            if (!passwordMatches) {
                return null;
            }
            if (!encoded) {
                upgradeLegacyPassword(id, storedPassword, PasswordHasher.hash(password));
            }
        } catch (GeneralSecurityException ex) {
            throw new SQLException("Could not verify or upgrade the staff password.", ex);
        }

        return new StaffAccount(id, storedUsername, fullName, role);
    }

    private void upgradeLegacyPassword(String staffId, String previousPassword, String encodedPassword)
            throws SQLException {
        String sql = "UPDATE tblStaff3 SET password = ? WHERE id = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, encodedPassword);
            statement.setString(2, staffId);
            statement.setString(3, previousPassword);
            statement.executeUpdate();
        }
    }
}
