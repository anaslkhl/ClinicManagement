package clinicmanagement.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * Salted password hashing for the credentials stored in {@code users.password}.
 *
 * <p>The scheme is PBKDF2-HMAC-SHA256: every hash gets its own random salt and
 * the result is stored in a self describing form so the parameters used to
 * produce it travel with the value:</p>
 *
 * <pre>{@code pbkdf2-sha256$<iterations>$<saltBase64>$<hashBase64>}</pre>
 *
 * <p>Plaintext passwords are never persisted, and {@link #matches(String, String)}
 * compares digests in constant time so a login attempt cannot be timed to
 * discover a password character by character.</p>
 */
public final class PasswordHasher {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    private static final String PREFIX = "pbkdf2-sha256";

    private static final int ITERATIONS = 210_000;

    private static final int KEY_LENGTH_BITS = 256;

    private static final int SALT_LENGTH_BYTES = 16;

    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordHasher() {
    }

    /**
     * Hashes a raw password with a freshly generated random salt.
     *
     * @param password the plaintext password, never {@code null} nor blank
     * @return the encoded hash to store in {@code users.password}
     */
    public static String hash(String password) {

        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password must be over 6 characters ");
        }

        byte[] salt = new byte[SALT_LENGTH_BYTES];
        RANDOM.nextBytes(salt);

        byte[] digest = derive(password, salt, ITERATIONS);

        return PREFIX + "$" + ITERATIONS + "$"
                + Base64.getEncoder().encodeToString(salt) + "$"
                + Base64.getEncoder().encodeToString(digest);
    }

    /**
     * Verifies a raw password against a stored hash.
     *
     * <p>A missing or unreadable hash is reported as a mismatch rather than as
     * an error: from the caller's point of view a row whose hash cannot be read
     * simply cannot be authenticated.</p>
     *
     * @param password    the plaintext password submitted by the user
     * @param encodedHash the hash previously produced by {@link #hash(String)}
     * @return {@code true} only when the password is the one behind the hash
     */
    public static boolean matches(String password, String encodedHash) {

        if (password == null || password.isEmpty() || encodedHash == null) {
            return false;
        }

        String[] parts = encodedHash.split("\\$");

        if (parts.length != 4 || !PREFIX.equals(parts[0])) {
            return false;
        }

        int iterations;

        try {
            iterations = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return false;
        }

        if (iterations <= 0) {
            return false;
        }

        byte[] salt;
        byte[] expected;

        try {
            salt = Base64.getDecoder().decode(parts[2]);
            expected = Base64.getDecoder().decode(parts[3]);
        } catch (IllegalArgumentException e) {
            return false;
        }

        byte[] actual = derive(password, salt, iterations);

        return MessageDigest.isEqual(expected, actual);
    }

    private static byte[] derive(String password, byte[] salt, int iterations) {

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, KEY_LENGTH_BITS);

        try {
            return SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("Unable to hash the password", e);
        } finally {
            spec.clearPassword();
        }
    }
}
