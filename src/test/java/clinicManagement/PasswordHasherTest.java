package clinicManagement;

import clinicmanagement.util.PasswordHasher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHasherTest {

    @Test
    @DisplayName("hash() never returns the password it was given")
    void shouldNotReturnThePlainPassword() {

        String hashed = PasswordHasher.hash("password123");

        assertNotEquals("password123", hashed);
        assertFalse(hashed.contains("password123"));
    }

    @Test
    @DisplayName("hash() salts every password, so two hashes of the same password differ")
    void shouldSaltEveryHash() {

        String first = PasswordHasher.hash("password123");
        String second = PasswordHasher.hash("password123");

        assertNotEquals(first, second, "a random salt must make the hashes differ");

        assertTrue(PasswordHasher.matches("password123", first));
        assertTrue(PasswordHasher.matches("password123", second));
    }

    @Test
    @DisplayName("hash() encodes the scheme, the iterations, the salt and the digest")
    void shouldEncodeItsParameters() {

        String hashed = PasswordHasher.hash("password123");

        String[] parts = hashed.split("\\$");

        assertEquals(4, parts.length);
        assertEquals("pbkdf2-sha256", parts[0]);
        assertTrue(Integer.parseInt(parts[1]) >= 100_000,
                "the iteration count must be high enough to slow a brute force down");
        assertTrue(parts[2].length() > 0);
        assertTrue(parts[3].length() > 0);
    }

    @Test
    @DisplayName("matches() accepts the password behind the hash")
    void shouldMatchTheRightPassword() {

        String hashed = PasswordHasher.hash("password123");

        assertTrue(PasswordHasher.matches("password123", hashed));
    }

    @Test
    @DisplayName("matches() refuses a wrong password, whatever its case or length")
    void shouldRefuseAWrongPassword() {

        String hashed = PasswordHasher.hash("password123");

        assertFalse(PasswordHasher.matches("password124", hashed));
        assertFalse(PasswordHasher.matches("PASSWORD123", hashed));
        assertFalse(PasswordHasher.matches("password12", hashed));
        assertFalse(PasswordHasher.matches("", hashed));
    }

    @Test
    @DisplayName("matches() refuses a hash that cannot be read instead of failing")
    void shouldRefuseAnUnreadableHash() {

        assertFalse(PasswordHasher.matches("password123", null));
        assertFalse(PasswordHasher.matches("password123", ""));
        assertFalse(PasswordHasher.matches("password123", "password123"));
        assertFalse(PasswordHasher.matches("password123", "pbkdf2-sha256$notanumber$c2FsdA==$aGFzaA=="));
        assertFalse(PasswordHasher.matches("password123", "pbkdf2-sha256$210000$not base64$aGFzaA=="));
        assertFalse(PasswordHasher.matches("password123", "md5$210000$c2FsdA==$aGFzaA=="));
        assertFalse(PasswordHasher.matches("password123", "pbkdf2-sha256$210000$c2FsdA=="));
        assertFalse(PasswordHasher.matches(null, PasswordHasher.hash("password123")));
    }

    @Test
    @DisplayName("hash() rejects an empty password")
    void shouldRejectAnEmptyPassword() {

        assertThrows(IllegalArgumentException.class, () -> PasswordHasher.hash(null));
        assertThrows(IllegalArgumentException.class, () -> PasswordHasher.hash(""));
    }
}
