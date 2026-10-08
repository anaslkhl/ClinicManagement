package clinicManagement.support;

import jakarta.servlet.http.HttpSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * {@link HttpSession} double recording the attributes written by a servlet and
 * whether it has been invalidated, so tests can assert what a servlet did to the
 * session of a request.
 *
 * <p>Like the other doubles of this package it is built on a JDK dynamic proxy:
 * only the handful of methods {@code UserServlet} calls are answered, every other
 * one returns a harmless default.</p>
 */
public final class FakeHttpSession {

    private final Map<String, Object> attributes = new LinkedHashMap<>();

    private final String id;

    private boolean invalidated;

    private FakeHttpSession(String id) {
        this.id = id;
    }

    public static FakeHttpSession create() {
        return new FakeHttpSession("fake-session-id");
    }

    public HttpSession getSession() {

        InvocationHandler handler = (proxy, method, args) -> {

            switch (method.getName()) {

                case "getAttribute":
                    return attributes.get(args[0]);

                case "setAttribute":
                    attributes.put((String) args[0], args[1]);
                    return null;

                case "removeAttribute":
                    attributes.remove(args[0]);
                    return null;

                case "getId":
                    return id;

                case "invalidate":
                    invalidated = true;
                    attributes.clear();
                    return null;

                case "isNew":
                    return false;

                case "toString":
                    return "FakeHttpSession";

                case "hashCode":
                    return System.identityHashCode(proxy);

                case "equals":
                    return proxy == args[0];

                default:
                    return null;
            }
        };

        return (HttpSession) Proxy.newProxyInstance(
                FakeHttpSession.class.getClassLoader(),
                new Class<?>[]{HttpSession.class},
                handler
        );
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public boolean isInvalidated() {
        return invalidated;
    }
}
