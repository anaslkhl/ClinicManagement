package clinicManagement;

import clinicmanagement.model.Role;
import clinicmanagement.model.User;
import clinicmanagement.service.AuthService;
import clinicmanagement.util.PasswordHasher;
import clinicManagement.support.FakeUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private FakeUserRepository userRepository;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository = new FakeUserRepository();
        authService = new AuthService(userRepository);
    }

    @Test
    @DisplayName("createUser() validates and delegates the persistence to the repository")
    void shouldCreateValidUser() {

        User user = validUser("valid@test.com");

        User created = authService.createUser(user);

        assertEquals(1, userRepository.getSaveCalls().size(),
                "the validated user must be handed to the repository exactly once");

        assertSame(user, userRepository.getSaveCalls().get(0));
        assertSame(user, created);

        assertNotNull(created.getId(), "the persisted user must carry a generated UUID");
    }

    @Test
    @DisplayName("createUser() rejects a null user without saving anything")
    void shouldRejectNullUser() {

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.createUser(null)
        );

        assertEquals("there is no client !", exception.getMessage());
        assertTrue(userRepository.getSaveCalls().isEmpty(),
                "an invalid user must never reach the repository");
    }

    @Test
    @DisplayName("createUser() rejects a missing email")
    void shouldRejectMissingEmail() {

        User user = new User("Amrani", "Ali", null, "0612345678", "password123", Role.PATIENT);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.createUser(user)
        );

        assertEquals("Email is required !", exception.getMessage());
        assertTrue(userRepository.getSaveCalls().isEmpty());
    }

    @Test
    @DisplayName("createUser() rejects a blank email")
    void shouldRejectBlankEmail() {

        User user = new User("Amrani", "Ali", "   ", "0612345678", "password123", Role.PATIENT);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.createUser(user)
        );

        assertEquals("Email is required !", exception.getMessage());
        assertTrue(userRepository.getSaveCalls().isEmpty());
    }

    @Test
    @DisplayName("createUser() rejects a malformed email")
    void shouldRejectMalformedEmail() {

        User user = new User("Amrani", "Ali", "ali.amrani", "0612345678", "password123", Role.PATIENT);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.createUser(user)
        );

        assertEquals("Invalid email", exception.getMessage());
        assertTrue(userRepository.getSaveCalls().isEmpty());
    }

    @Test
    @DisplayName("createUser() rejects a missing last name")
    void shouldRejectMissingName() {

        User user = new User(null, "Ali", "ali@test.com", "0612345678", "password123", Role.PATIENT);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.createUser(user)
        );

        assertEquals("Name is required !! ", exception.getMessage());
        assertTrue(userRepository.getSaveCalls().isEmpty());
    }

    @Test
    @DisplayName("createUser() rejects an empty last name")
    void shouldRejectEmptyName() {

        User user = new User("", "Ali", "ali@test.com", "0612345678", "password123", Role.PATIENT);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.createUser(user)
        );

        assertEquals("Name is required !! ", exception.getMessage());
        assertTrue(userRepository.getSaveCalls().isEmpty());
    }

    @Test
    @DisplayName("createUser() rejects a missing phone number")
    void shouldRejectMissingPhone() {

        User user = new User("Amrani", "Ali", "ali@test.com", null, "password123", Role.PATIENT);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.createUser(user)
        );

        assertEquals("Phone number is required !", exception.getMessage());
        assertTrue(userRepository.getSaveCalls().isEmpty());
    }

    @Test
    @DisplayName("createUser() rejects an empty phone number")
    void shouldRejectEmptyPhone() {

        User user = new User("Amrani", "Ali", "ali@test.com", "", "password123", Role.PATIENT);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.createUser(user)
        );

        assertEquals("Phone number is required !", exception.getMessage());
        assertTrue(userRepository.getSaveCalls().isEmpty());
    }

    @Test
    @DisplayName("createUser() rejects a missing password")
    void shouldRejectMissingPassword() {

        User user = new User("Amrani", "Ali", "ali@test.com", "0612345678", null, Role.PATIENT);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.createUser(user)
        );

        assertEquals("Password must be over 6 characters ", exception.getMessage());
        assertTrue(userRepository.getSaveCalls().isEmpty());
    }

    @Test
    @DisplayName("createUser() rejects a password under 6 characters")
    void shouldRejectShortPassword() {

        User user = new User("Amrani", "Ali", "ali@test.com", "0612345678", "12345", Role.PATIENT);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.createUser(user)
        );

        assertEquals("Password must be over 6 characters ", exception.getMessage());
        assertTrue(userRepository.getSaveCalls().isEmpty());
    }

    @Test
    @DisplayName("createUser() stores a hashed password instead of the submitted one")
    void shouldHashThePasswordBeforeSaving() {

        User user = validUser("hashed@test.com");

        User created = authService.createUser(user);

        assertNotEquals("password123", created.getPassword(),
                "the password must never reach the repository in clear text");

        assertTrue(PasswordHasher.matches("password123", created.getPassword()),
                "the stored password must still be the one of the user");

        assertTrue(created.getPassword().startsWith("pbkdf2-sha256$"),
                "the stored password must carry the hashing scheme and its parameters");
    }

    @Test
    @DisplayName("login() returns the user whose password matches")
    void shouldAuthenticateTheRightPassword() {

        User created = authService.createUser(validUser("login@test.com"));

        Optional<User> authenticated = authService.login("login@test.com", "password123");

        assertTrue(authenticated.isPresent());
        assertEquals(created.getId(), authenticated.get().getId());
    }

    @Test
    @DisplayName("login() ignores the case and the spaces around the email")
    void shouldTrimTheEmail() {

        authService.createUser(validUser("trimmed@test.com"));

        assertTrue(authService.login("  trimmed@test.com  ", "password123").isPresent());
    }

    @Test
    @DisplayName("login() refuses a wrong password")
    void shouldRefuseAWrongPassword() {

        authService.createUser(validUser("wrong-password@test.com"));

        assertTrue(authService.login("wrong-password@test.com", "password124").isEmpty());
    }

    @Test
    @DisplayName("login() refuses an unknown email")
    void shouldRefuseAnUnknownEmail() {

        assertTrue(authService.login("ghost@test.com", "password123").isEmpty());
    }

    @Test
    @DisplayName("login() refuses missing credentials without asking the repository")
    void shouldRefuseMissingCredentials() {

        authService.createUser(validUser("blank@test.com"));

        assertTrue(authService.login(null, "password123").isEmpty());
        assertTrue(authService.login("  ", "password123").isEmpty());
        assertTrue(authService.login("blank@test.com", null).isEmpty());
        assertTrue(authService.login("blank@test.com", "  ").isEmpty());
    }

    @Test
    @DisplayName("login() refuses a deactivated account")
    void shouldRefuseADeactivatedUser() {

        User created = authService.createUser(validUser("inactive@test.com"));
        created.setActive(false);

        assertTrue(authService.login("inactive@test.com", "password123").isEmpty());
    }

    private User validUser(String email) {
        return new User("Amrani", "Ali", email, "0612345678", "password123", Role.PATIENT);
    }
}