package clinicManagement.support;

import clinicmanagement.model.User;
import clinicmanagement.repository.UserRepository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * In-memory {@link UserRepository} used to observe the calls made by the
 * service under test, without touching the real database.
 *
 * It mimics {@code UserRepositoryImpl} closely enough for unit tests:
 * it generates a UUID on first save and keeps the stored users addressable
 * by id and by email.
 */
public class FakeUserRepository implements UserRepository {

    private final Map<UUID, User> storedUsers = new LinkedHashMap<>();

    /** Every value handed to {@link #save(User)}, in call order. */
    private final List<User> saveCalls = new ArrayList<>();

    /** Value returned by {@link #save(User)}, overridable to simulate failures. */
    private User saveResult;

    @Override
    public User save(User user) {

        saveCalls.add(user);

        if (user.getId() == null) {
            assignId(user, UUID.randomUUID());
        }

        storedUsers.put(user.getId(), user);

        return saveResult != null ? saveResult : user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(storedUsers.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return storedUsers.values()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(storedUsers.values());
    }

    @Override
    public void delete(User user) {
        storedUsers.remove(user.getId());
    }

    @Override
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    public List<User> getSaveCalls() {
        return saveCalls;
    }

    public void setSaveResult(User saveResult) {
        this.saveResult = saveResult;
    }

    /**
     * The real repository lets Hibernate generate the identifier, which the
     * plain {@code User} class does not expose a setter for. Reflection keeps
     * the fake honest without touching production code.
     */
    private void assignId(User user, UUID id) {
        try {
            Field idField = User.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(user, id);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to assign a generated id", e);
        }
    }
}