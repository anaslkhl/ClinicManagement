package clinicManagement.support;

import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal {@link HttpServletRequest} double built on a JDK dynamic proxy so the
 * tests need no mocking framework. Only the handful of methods that
 * {@code UserServlet} actually calls are answered; everything else returns a
 * harmless default.
 *
 * <p>Attributes written by the servlet are readable through {@link #getAttribute(String)}
 * and the dispatchers it obtained are readable through {@link #getDispatcher(String)}.
 * The session the servlet opened, invalidated or ignored is readable through
 * {@link #getSession()}.</p>
 */
public final class StubServletRequest {

    private final Map<String, String> parameters;

    private final String method;

    private final String servletPath;

    private final Map<String, Object> attributes = new LinkedHashMap<>();

    private final Map<String, FakeRequestDispatcher> dispatchers = new LinkedHashMap<>();

    private FakeHttpSession session;

    private StubServletRequest(Map<String, String> parameters, String method, String servletPath) {
        this.parameters = parameters;
        this.method = method;
        this.servletPath = servletPath;
    }

    public static StubServletRequest withParameters(Map<String, String> parameters) {
        return new StubServletRequest(parameters, "POST", null);
    }

    /**
     * @param method      the HTTP method, {@code "GET"} or {@code "POST"}
     * @param servletPath the url pattern the request matched, the value a servlet
     *                    reads to tell its own endpoints apart; {@code null} when
     *                    the request is meant to reach the registration endpoint
     */
    public static StubServletRequest withParameters(
            Map<String, String> parameters, String method, String servletPath) {
        return new StubServletRequest(parameters, method, servletPath);
    }

    /** Seeds the session the request already carries, as a browser would send it back. */
    public void setSession(FakeHttpSession session) {
        this.session = session;
    }

    /** The session of this request: the seeded one, or the one the servlet opened. */
    public FakeHttpSession getSession() {
        return session;
    }

    public HttpServletRequest getRequest() {

        InvocationHandler handler = (proxy, called, args) -> {

            switch (called.getName()) {

                case "getParameter":
                    return parameters.get(args[0]);

                case "setAttribute":
                    attributes.put((String) args[0], args[1]);
                    return null;

                case "getAttribute":
                    return attributes.get(args[0]);

                case "getContextPath":
                    return "";

                case "getMethod":
                    return method;

                case "getServletPath":
                    return servletPath;

                case "getSession":
                    return resolveSession(args);

                case "getRequestDispatcher": {

                    String path = (String) args[0];
                    FakeRequestDispatcher dispatcher = new FakeRequestDispatcher(path);
                    dispatchers.put(path, dispatcher);
                    return dispatcher;
                }

                case "toString":
                    return "StubServletRequest";

                case "hashCode":
                    return System.identityHashCode(proxy);

                case "equals":
                    return proxy == args[0];

                default:
                    return defaultValue(called);
            }
        };

        return (HttpServletRequest) Proxy.newProxyInstance(
                StubServletRequest.class.getClassLoader(),
                new Class<?>[]{HttpServletRequest.class},
                handler
        );
    }

    /**
     * Mirrors the container contract: {@code getSession(true)} creates a session
     * when there is none left, {@code getSession(false)} and {@code getSession()}
     * answer {@code null} once the current session has been invalidated.
     */
    private Object resolveSession(Object[] args) {

        boolean create = args != null && args.length > 0 && Boolean.TRUE.equals(args[0]);

        if (session != null && session.isInvalidated()) {
            session = null;
        }

        if (session == null && create) {
            session = FakeHttpSession.create();
        }

        return session == null ? null : session.getSession();
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    /** The dispatcher handed out for {@code path}, or {@code null} if never requested. */
    public FakeRequestDispatcher getDispatcher(String path) {
        return dispatchers.get(path);
    }

    private static Object defaultValue(Method method) {

        Class<?> returnType = method.getReturnType();

        if (!returnType.isPrimitive() || returnType == void.class) {
            return null;
        }
        if (returnType == boolean.class) {
            return false;
        }
        if (returnType == int.class) {
            return 0;
        }
        if (returnType == long.class) {
            return 0L;
        }
        return null;
    }
}