package clinicManagement;

import clinicmanagement.config.DatabaseConnection;
import clinicmanagement.model.Role;
import clinicmanagement.model.User;
import clinicmanagement.repository.UserRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private static final String EMAIL_PREFIX = "repo-";

    private UserRepositoryImpl userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryImpl(DatabaseConnection.getEntityManagerFactory());
    }

    /**
     * These tests run against the real database, so the rows they create are
     * removed afterwards to keep the suite repeatable.
     */
    @AfterEach
    void tearDown() {

        List<String> emails = userRepository.findAll()
                .stream()
                .map(User::getEmail)
                .filter(email -> email.startsWith(EMAIL_PREFIX))
                .toList();

        for (String email : emails) {
            userRepository.findByEmail(email).ifPresent(userRepository::delete);
        }
    }

    @Test
    @DisplayName("save() persists the user and generates a UUID")
    void shouldSaveUserAndGenerateUuid() {

        String email = uniqueEmail();

        User user = newUser(email);

        User saved = userRepository.save(user);

        assertNotNull(saved.getId(), "save() must return a user carrying a generated UUID");

        Optional<User> found = userRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(email, found.get().getEmail());
        assertEquals("Amrani", found.get().getNom());
    }

    @Test
    @DisplayName("findById() returns the stored user")
    void shouldFindUserById() {

        User saved = userRepository.save(newUser(uniqueEmail()));

        Optional<User> found = userRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
    }

    @Test
    @DisplayName("findById() returns empty for an unknown id")
    void shouldReturnEmptyWhenFindingByUnknownId() {

        Optional<User> found = userRepository.findById(UUID.randomUUID());

        assertTrue(found.isEmpty(), "an unknown id must not resolve to a user");
    }

    @Test
    @DisplayName("findByEmail() returns the stored user")
    void shouldFindUserByEmail() {

        String email = uniqueEmail();

        userRepository.save(newUser(email));

        Optional<User> found = userRepository.findByEmail(email);

        assertTrue(found.isPresent());
        assertEquals(email, found.get().getEmail());
    }

    @Test
    @DisplayName("findByEmail() returns empty for an unknown email")
    void shouldReturnEmptyWhenFindingByUnknownEmail() {

        Optional<User> found = userRepository.findByEmail(uniqueEmail());

        assertTrue(found.isEmpty(), "an unknown email must not resolve to a user");
    }

    @Test
    @DisplayName("findAll() returns every stored user")
    void shouldFindAllUsers() {

        List<User> before = userRepository.findAll();

        String firstEmail = uniqueEmail();
        String secondEmail = uniqueEmail();

        userRepository.save(newUser(firstEmail));
        userRepository.save(newUser(secondEmail));

        List<User> after = userRepository.findAll();

        assertEquals(before.size() + 2, after.size());
        assertTrue(after.stream().anyMatch(u -> u.getEmail().equals(firstEmail)));
        assertTrue(after.stream().anyMatch(u -> u.getEmail().equals(secondEmail)));

        userRepository.delete(userRepository.findByEmail(firstEmail).orElseThrow());
        userRepository.delete(userRepository.findByEmail(secondEmail).orElseThrow());
    }

    @Test
    @DisplayName("existsByEmail() detects an email that is already taken")
    void shouldDetectExistingEmail() {

        String email = uniqueEmail();

        assertFalse(userRepository.existsByEmail(email));

        userRepository.save(newUser(email));

        assertTrue(userRepository.existsByEmail(email));

        userRepository.delete(userRepository.findByEmail(email).orElseThrow());
    }

    @Test
    @DisplayName("existsByEmail() returns false for a free email")
    void shouldNotDetectUnknownEmail() {

        assertFalse(userRepository.existsByEmail(uniqueEmail()));
    }

    @Test
    @DisplayName("delete() removes the user from the database")
    void shouldDeleteUser() {

        String email = uniqueEmail();

        User saved = userRepository.save(newUser(email));

        assertTrue(userRepository.existsByEmail(email));

        userRepository.delete(saved);

        assertFalse(userRepository.existsByEmail(email));
        assertTrue(userRepository.findById(saved.getId()).isEmpty());
        assertTrue(userRepository.findByEmail(email).isEmpty());
    }

    @Test
    @DisplayName("delete() of a detached instance still removes the user")
    void shouldDeleteDetachedUser() {

        String email = uniqueEmail();

        User saved = userRepository.save(newUser(email));

        User detached = new User(
                saved.getNom(),
                saved.getPrenom(),
                saved.getEmail(),
                saved.getTelephone(),
                saved.getPassword(),
                saved.getRole()
        );

        setId(detached, saved.getId());

        userRepository.delete(detached);

        assertFalse(userRepository.existsByEmail(email));
    }

    private User newUser(String email) {
        return new User(
                "Amrani",
                "Ali",
                email,
                "0612345678",
                "password123",
                Role.PATIENT
        );
    }

    private String uniqueEmail() {
        return EMAIL_PREFIX + UUID.randomUUID() + "@test.com";
    }

    /**
     * The {@code save()}/{@code findById()} round trip already proves the id
     * round-trips correctly. This copy only exists to exercise the
     * {@code em.merge(...)} branch of {@code delete()}, which needs an instance
     * the repository has never managed itself.
     */
    private void setId(User user, UUID id) {
        try {
            var field = User.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(user, id);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to copy the id", e);
        }
    }
}