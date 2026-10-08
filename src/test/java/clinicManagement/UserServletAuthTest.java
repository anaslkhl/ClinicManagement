package clinicManagement;

import clinicmanagement.controller.UserServlet;
import clinicmanagement.model.Role;
import clinicmanagement.model.User;
import clinicmanagement.service.AuthService;
import clinicManagement.support.FakeHttpSession;
import clinicManagement.support.FakeRequestDispatcher;
import clinicManagement.support.FakeUserRepository;
import clinicManagement.support.StubServletConfig;
import clinicManagement.support.StubServletRequest;
import clinicManagement.support.StubServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Register, login and logout as they are wired in {@link UserServlet}, driven
 * through the stubbed request and response of the {@code support} package so no
 * database is needed here.
 */
class UserServletAuthTest {

    private static final String CREATE_PATH = "/users/create";

    private static final String LOGIN_PATH = "/users/login";

    private static final String LOGOUT_PATH = "/users/logout";

    private static final String SESSION_USER = "user";

    private static final String SESSION_ROLE = "role";

    private static final String LOGIN_FAILED = "Invalid email or password !";

    private static final String LOGIN_FORM_PATH = "/WEB-INF/views/login-form.jsp";

    private static final String USER_FORM_PATH = "/WEB-INF/views/user-form.jsp";

    private static final String PASSWORD = "password123";

    private static final String EMAIL = "auth@test.com";

    private FakeUserRepository userRepository;

    private AuthService authService;

    private UserServlet userServlet;

    private HttpServletResponse response;

    private StubServletResponse.RedirectRecorder recorder;

    @BeforeEach
    void setUp() throws Exception {

        userRepository = new FakeUserRepository();
        authService = new AuthService(userRepository);
        userServlet = new UserServlet();
        userServlet.init(StubServletConfig.create(authService));

        recorder = new StubServletResponse.RedirectRecorder();
        response = StubServletResponse.create(recorder);
    }

    @Test
    @DisplayName("POST /users/create registers the user with a hashed password")
    void shouldRegisterWithAHashedPassword() throws Exception {

        String email = "register-" + UUID.randomUUID() + "@test.com";

        StubServletRequest stub = StubServletRequest.withParameters(form(email));

        post(stub);

        User stored = userRepository.findByEmail(email).orElseThrow();

        assertNotEquals(PASSWORD, stored.getPassword(),
                "the password must be stored hashed");

        assertEquals(1, recorder.getRedirects().size());
        assertEquals("/users", recorder.getRedirects().get(0));
    }

    @Test
    @DisplayName("GET /users/create forwards to the registration form")
    void shouldShowTheRegistrationForm() throws Exception {

        StubServletRequest stub = StubServletRequest.withParameters(Map.of(), "GET", CREATE_PATH);

        get(stub);

        assertForwardedTo(stub, USER_FORM_PATH);
    }

    @Test
    @DisplayName("GET /users/login forwards to the login form")
    void shouldShowTheLoginForm() throws Exception {

        StubServletRequest stub = StubServletRequest.withParameters(Map.of(), "GET", LOGIN_PATH);

        get(stub);

        assertForwardedTo(stub, LOGIN_FORM_PATH);
    }

    @Test
    @DisplayName("POST /users/login opens a session holding the authenticated user")
    void shouldOpenASessionOnLogin() throws Exception {

        register(EMAIL);

        StubServletRequest stub = loginRequest(EMAIL, PASSWORD);

        post(stub);

        FakeHttpSession session = stub.getSession();

        assertNotNull(session, "a successful login must open a session");

        User sessionUser = (User) session.getAttribute(SESSION_USER);

        assertNotNull(sessionUser, "the session must carry the user");
        assertEquals(EMAIL, sessionUser.getEmail());
        assertEquals(Role.PATIENT.name(), session.getAttribute(SESSION_ROLE));

        assertFalse(session.isInvalidated(), "the session of the login must stay open");

        assertEquals(1, recorder.getRedirects().size());
        assertEquals("/", recorder.getRedirects().get(0));
    }

    @Test
    @DisplayName("POST /users/login drops the session opened before the login")
    void shouldReplaceTheSessionOnLogin() throws Exception {

        register(EMAIL);

        FakeHttpSession previous = FakeHttpSession.create();

        StubServletRequest stub = loginRequest(EMAIL, PASSWORD);
        stub.setSession(previous);

        post(stub);

        assertTrue(previous.isInvalidated(),
                "the session that existed before the login must not be reused");

        assertNotSame(previous, stub.getSession(), "the login must open a fresh session");
        assertNotNull(stub.getSession().getAttribute(SESSION_USER));
    }

    @Test
    @DisplayName("POST /users/login refuses a wrong password and opens no session")
    void shouldRefuseAWrongPassword() throws Exception {

        register(EMAIL);

        StubServletRequest stub = loginRequest(EMAIL, "wrong-password");

        post(stub);

        assertEquals(LOGIN_FAILED, stub.getAttribute("error"));
        assertEquals(EMAIL, stub.getAttribute("email"), "the email must survive a refused login");

        assertTrue(recorder.getRedirects().isEmpty(), "a refused login must not redirect");
        assertForwardedTo(stub, LOGIN_FORM_PATH);
        assertNull(stub.getSession(), "a refused login must not open a session");
    }

    @Test
    @DisplayName("POST /users/login refuses an unknown email like a wrong password")
    void shouldRefuseAnUnknownEmail() throws Exception {

        StubServletRequest stub = loginRequest("ghost@test.com", PASSWORD);

        post(stub);

        assertEquals(LOGIN_FAILED, stub.getAttribute("error"));
        assertNull(stub.getSession());
        assertForwardedTo(stub, LOGIN_FORM_PATH);
    }

    @Test
    @DisplayName("POST /users/login refuses a deactivated account")
    void shouldRefuseADeactivatedAccount() throws Exception {

        User user = register(EMAIL);
        user.setActive(false);

        StubServletRequest stub = loginRequest(EMAIL, PASSWORD);

        post(stub);

        assertEquals(LOGIN_FAILED, stub.getAttribute("error"));
        assertNull(stub.getSession());
    }

    @Test
    @DisplayName("GET /users/logout invalidates the session and sends the user back to the login form")
    void shouldInvalidateTheSessionOnLogout() throws Exception {

        StubServletRequest stub = StubServletRequest.withParameters(Map.of(), "GET", LOGOUT_PATH);
        stub.setSession(FakeHttpSession.create());

        get(stub);

        assertTrue(stub.getSession() == null || stub.getSession().isInvalidated(),
                "the session must be dropped");

        assertEquals(1, recorder.getRedirects().size());
        assertEquals(LOGIN_PATH, recorder.getRedirects().get(0));
    }

    @Test
    @DisplayName("GET /users/logout without a session still lands on the login form")
    void shouldLogoutWithoutASession() throws Exception {

        StubServletRequest stub = StubServletRequest.withParameters(Map.of(), "GET", LOGOUT_PATH);

        get(stub);

        assertNull(stub.getSession());
        assertEquals(LOGIN_PATH, recorder.getRedirects().get(0));
    }

    /**
     * Goes through {@code HttpServlet.service(..)}, the public entry point a
     * container calls, which routes the stubbed request to {@code doGet(..)} or
     * {@code doPost(..)}.
     */
    private void post(StubServletRequest stub) throws Exception {
        userServlet.service(stub.getRequest(), response);
    }

    private void get(StubServletRequest stub) throws Exception {
        userServlet.service(stub.getRequest(), response);
    }

    /** Registers a user the way the registration endpoint does, password hashed included. */
    private User register(String email) {
        return authService.createUser(
                new User("Amrani", "Ali", email, "0612345678", PASSWORD, Role.PATIENT));
    }

    private StubServletRequest loginRequest(String email, String password) {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("email", email);
        parameters.put("password", password);

        return StubServletRequest.withParameters(parameters, "POST", LOGIN_PATH);
    }

    private void assertForwardedTo(StubServletRequest stub, String path) {

        FakeRequestDispatcher dispatcher = stub.getDispatcher(path);

        assertNotNull(dispatcher, "the servlet must request " + path);
        assertEquals(List.of(path), dispatcher.getForwardPaths(),
                "the request must be forwarded to " + path + " exactly once");
    }

    private Map<String, String> form(String email) {

        Map<String, String> parameters = new HashMap<>();

        parameters.put("nom", "Amrani");
        parameters.put("prenom", "Ali");
        parameters.put("email", email);
        parameters.put("telephone", "0612345678");
        parameters.put("password", PASSWORD);

        return parameters;
    }
}
