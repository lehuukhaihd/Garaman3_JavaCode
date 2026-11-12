package com.garaman3;

import com.garaman3.entity.StaffAccount;
import com.garaman3.util.PasswordHasher;

public class PasswordHasherTest {

    public static void main(String[] args) throws Exception {
        String testPassword = "test-manager-password";
        String testHash = PasswordHasher.hash(testPassword);
        check(PasswordHasher.matches(testPassword, testHash),
                "password matches its PBKDF2 hash");
        check(!PasswordHasher.matches("wrong-password", testHash),
                "wrong password is rejected");
        check(!PasswordHasher.matches("password", "pbkdf2_sha256$bad$salt$hash"),
                "malformed encoded password is rejected");

        String firstHash = PasswordHasher.hash("new-password");
        String secondHash = PasswordHasher.hash("new-password");
        check(!firstHash.equals(secondHash), "new hashes use independent salts");
        check(PasswordHasher.matches("new-password", firstHash), "new hash verifies");
        check(!PasswordHasher.matches("other-password", firstHash), "new hash rejects a different password");
        check(PasswordHasher.matchesLegacyPassword("legacy", "legacy"),
                "existing plaintext staff password can be upgraded after a successful login");
        check(!PasswordHasher.matchesLegacyPassword("wrong", "legacy"),
                "incorrect legacy password is rejected");

        check(new StaffAccount("1", "manager", "Manager", "Manager").canViewSupplierReports(),
                "Manager role may view supplier reports");
        check(!new StaffAccount("2", "warehouse", "Warehouse staff", "WarehouseStaff")
                        .canViewSupplierReports(),
                "Warehouse role may not view supplier reports");
        System.out.println("All password and role checks passed.");
    }

    private static void check(boolean condition, String label) {
        if (!condition) {
            throw new AssertionError(label);
        }
    }
}
