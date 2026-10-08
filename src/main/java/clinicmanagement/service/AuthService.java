package clinicmanagement.service;

import clinicmanagement.Validation.EmailValidation;
import clinicmanagement.Validation.NameValidation;
import clinicmanagement.Validation.PasswordValidation;
import clinicmanagement.Validation.PhoneValidation;
import clinicmanagement.model.User;
import clinicmanagement.repository.UserRepository;
import clinicmanagement.util.PasswordHasher;

import java.util.Optional;


public class AuthService {


    private static final String ABSENT_USER_HASH = PasswordHasher.hash("clinic-management-absent-user");

    private UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User createUser(User user) {

        if (user == null) {
            throw new IllegalArgumentException("there is no client !");
        }

        EmailValidation.validateEmail(user.getEmail());
        NameValidation.validateName(user.getNom());
        PhoneValidation.validatePhone(user.getTelephone());
        PasswordValidation.validatePassword(user.getPassword());

        user.setPassword(PasswordHasher.hash(user.getPassword()));

        userRepository.save(user);

        return user;
    }

    
    public Optional<User> login(String email, String password) {

        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            return Optional.empty();
        }

        Optional<User> candidate = userRepository.findByEmail(email.trim());

        if (candidate.isEmpty()) {
            PasswordHasher.matches(password, ABSENT_USER_HASH);
            return Optional.empty();
        }

        User user = candidate.get();

        if (!user.isActive() || !PasswordHasher.matches(password, user.getPassword())) {
            return Optional.empty();
        }

        return Optional.of(user);
    }
}
