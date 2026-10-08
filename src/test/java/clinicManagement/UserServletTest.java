package clinicManagement;

import clinicmanagement.config.DatabaseConnection;
import clinicmanagement.controller.UserServlet;
import clinicmanagement.model.Role;
import clinicmanagement.model.User;
import clinicmanagement.repository.UserRepositoryImpl;
import clinicmanagement.service.AuthService;
import clinicmanagement.util.PasswordHasher;
import clinicManagement.support.FakeUserRepository;
import clinicManagement.support.FakeRequestDispatcher;
import clinicManagement.support.StubServletConfig;
import clinicManagement.support.StubServletRequest;
import clinicManagement.support.StubServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserServletTest {

    private static final String USER_FORM_PATH = "/WEB-INF/views/user-form.jsp";

    private static final String EMAIL_PREFIX = "servlet-";

    private UserRepositoryImpl realRepository;

    private UserServlet userServlet;

    private HttpServletResponse response;

    private StubServletResponse.RedirectRecorder recorder;

    @BeforeEach
    void setUp() throws Exception {
        realRepository = new UserRepositoryImpl(DatabaseConnection.getEntityManagerFactory());
        userServlet = new UserServlet();
        userServlet.init(StubServletConfig.create(new AuthService(realRepository)));

        recorder = new StubServletResponse.RedirectRecorder();
        response = StubServletResponse.create(recorder);
    }

    /**
     * The valid-user cases hit the real database, so the rows they create are
     * removed afterwards to keep the suite repeatable.
     */
    @AfterEach
    void tearDown() {

        List<String> emails = realRepository.findAll()
                .stream()
                .map(User::getEmail)
                .filter(email -> email.startsWith(EMAIL_PREFIX))
                .toList();

        for (String email : emails) {
            realRepository.findByEmail(email).ifPresent(realRepository::delete);
        }
    }

    @Test
    @DisplayName("doPost() creates the user and redirects to /users")
    void shouldCreateUserAndRedirect() throws Exception {

        String email = uniqueEmail();

        post(StubServletRequest.withParameters(form(email)));

        assertNotNull(realRepository.findByEmail(email).orElse(null),
                "the submitted user must be persisted");

        var created = realRepository.findByEmail(email).orElseThrow();

        assertEquals("Amrani", created.getNom());
        assertEquals("Ali", created.getPrenom());
        assertEquals("0612345678", created.getTelephone());
        assertEquals(Role.PATIENT, created.getRole());

        assertNotNull(created.getId(), "the created user must carry a generated UUID");

        assertEquals(1, recorder.getRedirects().size());
        assertEquals("/users", recorder.getRedirects().get(0));
    }

    @Test
    @DisplayName("doPost() rejects an invalid email and forwards the error to the form")
    void shouldRejectInvalidEmail() throws Exception {

        Map<String, String> parameters = form(EMAIL_PREFIX + "ali.amrani");

        StubServletRequest stub = StubServletRequest.withParameters(parameters);

        post(stub);

        assertNull(realRepository.findByEmail(parameters.get("email")).orElse(null),
                "an invalid user must never be saved");

        assertEquals("Invalid email", stub.getAttribute("error"));

        assertTrue(recorder.getRedirects().isEmpty(),
                "an invalid user must not be answered with a redirect");

        assertForwardedToUserForm(stub);
    }

    @Test
    @DisplayName("doPost() rejects a missing email and forwards the error to the form")
    void shouldRejectMissingEmail() throws Exception {

        Map<String, String> parameters = form(uniqueEmail());
        parameters.put("email", "");

        StubServletRequest stub = StubServletRequest.withParameters(parameters);

        post(stub);

        assertEquals("Email is required !", stub.getAttribute("error"));
        assertTrue(recorder.getRedirects().isEmpty());
        assertForwardedToUserForm(stub);
    }

    @Test
    @DisplayName("doPost() rejects a missing last name and forwards the error to the form")
    void shouldRejectMissingName() throws Exception {

        Map<String, String> parameters = form(uniqueEmail());
        parameters.put("nom", "");

        StubServletRequest stub = StubServletRequest.withParameters(parameters);

        post(stub);

        assertNull(realRepository.findByEmail(parameters.get("email")).orElse(null),
                "an invalid user must never be saved");

        assertEquals("Name is required !! ", stub.getAttribute("error"));
        assertTrue(recorder.getRedirects().isEmpty());
        assertForwardedToUserForm(stub);
    }

    @Test
    @DisplayName("doPost() rejects a missing phone number and forwards the error to the form")
    void shouldRejectMissingPhone() throws Exception {

        Map<String, String> parameters = form(uniqueEmail());
        parameters.put("telephone", "");

        StubServletRequest stub = StubServletRequest.withParameters(parameters);

        post(stub);

        assertNull(realRepository.findByEmail(parameters.get("email")).orElse(null),
                "an invalid user must never be saved");

        assertEquals("Phone number is required !", stub.getAttribute("error"));
        assertTrue(recorder.getRedirects().isEmpty());
        assertForwardedToUserForm(stub);
    }

    @Test
    @DisplayName("doPost() rejects a short password and forwards the error to the form")
    void shouldRejectShortPassword() throws Exception {

        Map<String, String> parameters = form(uniqueEmail());
        parameters.put("password", "12345");

        StubServletRequest stub = StubServletRequest.withParameters(parameters);

        post(stub);

        assertNull(realRepository.findByEmail(parameters.get("email")).orElse(null),
                "an invalid user must never be saved");

        assertEquals("Password must be over 6 characters ", stub.getAttribute("error"));
        assertTrue(recorder.getRedirects().isEmpty());
        assertForwardedToUserForm(stub);
    }

    @Test
    @DisplayName("existsByEmail() detects a duplicate email and the second registration is rejected")
    void shouldRejectDuplicateEmail() throws Exception {

        String email = uniqueEmail();

        post(StubServletRequest.withParameters(form(email)));

        assertTrue(realRepository.existsByEmail(email),
                "the first registration must make the email taken");

        assertEquals("/users", recorder.getRedirects().get(0));

        UUID firstId = realRepository.findByEmail(email).orElseThrow().getId();

        recorder = new StubServletResponse.RedirectRecorder();
        response = StubServletResponse.create(recorder);

        assertThrows(RuntimeException.class,
                () -> post(StubServletRequest.withParameters(form(email))),
                "the unique constraint on users.email must reject the duplicate");

        assertEquals(firstId, realRepository.findByEmail(email).orElseThrow().getId(),
                "the first user must be left untouched");

        assertTrue(recorder.getRedirects().isEmpty(),
                "a rejected duplicate must not be answered with a redirect");
    }

    @Test
    @DisplayName("doPost() persists the password hashed, so it still fits the column and verifies")
    void shouldPersistAHashedPassword() throws Exception {

        String email = uniqueEmail();

        post(StubServletRequest.withParameters(form(email)));

        String stored = realRepository.findByEmail(email).orElseThrow().getPassword();

        assertNotEquals("password123", stored,
                "the password must never be persisted in clear text");

        assertTrue(stored.startsWith("pbkdf2-sha256$"),
                "the persisted password must carry the hashing scheme and its parameters");

        assertTrue(PasswordHasher.matches("password123", stored),
                "the persisted hash must not be truncated by the column, or login could never succeed");
    }

    @Test
    @DisplayName("AuthService leaves the repository untouched when the user is invalid")
    void shouldNotSaveInvalidUser() throws Exception {

        var userRepository = new FakeUserRepository();

        AuthService service = new AuthService(userRepository);

        userServlet = new UserServlet();
        userServlet.init(StubServletConfig.create(service));

        post(StubServletRequest.withParameters(form(EMAIL_PREFIX + "not-an-email")));

        assertTrue(userRepository.getSaveCalls().isEmpty(),
                "no invalid user may reach the repository");
    }

    /**
     * Goes through {@code HttpServlet.service(..)}, the public entry point a
     * container calls, which routes the stubbed POST to {@code doPost(..)}.
     * {@code doPost(..)} itself is protected, so the test cannot call it
     * directly from this package.
     */
    private void post(StubServletRequest stub) throws Exception {
        userServlet.service(stub.getRequest(), response);
    }

    private void assertForwardedToUserForm(StubServletRequest stub) {

        FakeRequestDispatcher dispatcher = stub.getDispatcher(USER_FORM_PATH);

        assertNotNull(dispatcher, "the servlet must request " + USER_FORM_PATH);
        assertEquals(1, dispatcher.getForwardPaths().size(),
                "the error must be forwarded to the form exactly once");
        assertEquals(USER_FORM_PATH, dispatcher.getForwardPaths().get(0));
    }

    private Map<String, String> form(String email) {

        Map<String, String> parameters = new HashMap<>();

        parameters.put("nom", "Amrani");
        parameters.put("prenom", "Ali");
        parameters.put("email", email);
        parameters.put("telephone", "0612345678");
        parameters.put("password", "password123");

        return parameters;
    }

    private String uniqueEmail() {
        return EMAIL_PREFIX + UUID.randomUUID() + "@test.com";
    }
}