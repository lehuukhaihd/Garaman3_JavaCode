package com.garaman3.util;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordHasher {

    private static final String PREFIX = "pbkdf2_sha256$";
    private static final int ITERATIONS = 210_000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;
    private static final int MAX_ACCEPTED_ITERATIONS = 1_000_000;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordHasher() {
    }

    public static String hash(String password) throws GeneralSecurityException {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null.");
        }
        byte[] salt = new byte[SALT_LENGTH];
        RANDOM.nextBytes(salt);
        byte[] derivedKey = deriveKey(password, salt, ITERATIONS);
        return PREFIX + ITERATIONS + "$"
                + Base64.getEncoder().encodeToString(salt) + "$"
                + Base64.getEncoder().encodeToString(derivedKey);
    }

    public static boolean isEncoded(String value) {
        return value != null && value.startsWith("pbkdf2_");
    }

    public static boolean matches(String password, String encoded)
            throws GeneralSecurityException {
        if (password == null || encoded == null) {
            return false;
        }

        String[] parts = encoded.split("\\$", -1);
        if (parts.length != 4 || !"pbkdf2_sha256".equals(parts[0])) {
            return false;
        }

        try {
            int iterations = Integer.parseInt(parts[1]);
            byte[] salt = Base64.getDecoder().decode(parts[2]);
            byte[] expectedKey = Base64.getDecoder().decode(parts[3]);
            if (iterations < 1 || iterations > MAX_ACCEPTED_ITERATIONS
                    || salt.length < 8 || salt.length > 64 || expectedKey.length != KEY_LENGTH / 8) {
                return false;
            }
            byte[] actualKey = deriveKey(password, salt, iterations);
            return MessageDigest.isEqual(expectedKey, actualKey);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static boolean matchesLegacyPassword(String password, String storedPassword) {
        if (password == null || storedPassword == null) {
            return false;
        }
        return MessageDigest.isEqual(
                password.getBytes(StandardCharsets.UTF_8),
                storedPassword.getBytes(StandardCharsets.UTF_8));
    }

    private static byte[] deriveKey(String password, byte[] salt, int iterations)
            throws GeneralSecurityException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, KEY_LENGTH);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec)
                    .getEncoded();
        } finally {
            spec.clearPassword();
        }
    }
}
