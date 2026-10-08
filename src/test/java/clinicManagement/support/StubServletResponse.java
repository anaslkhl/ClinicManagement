package clinicManagement.support;

import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * Minimal {@link HttpServletResponse} double recording redirects.
 */
public final class StubServletResponse {

    private StubServletResponse() {
    }

    public static HttpServletResponse create(RedirectRecorder recorder) {

        InvocationHandler handler = (proxy, method, args) -> {

            switch (method.getName()) {

                case "sendRedirect":
                    recorder.redirects.add((String) args[0]);
                    return null;

                case "setStatus":
                    recorder.status = (Integer) args[0];
                    return null;

                case "getStatus":
                    return recorder.status;

                case "toString":
                    return "StubServletResponse";

                case "hashCode":
                    return System.identityHashCode(proxy);

                case "equals":
                    return proxy == args[0];

                default:
                    return null;
            }
        };

        return (HttpServletResponse) Proxy.newProxyInstance(
                StubServletResponse.class.getClassLoader(),
                new Class<?>[]{HttpServletResponse.class},
                handler
        );
    }

    /** Mutable holder so assertions can inspect what the servlet did. */
    public static class RedirectRecorder {

        private final List<String> redirects = new ArrayList<>();

        private int status = HttpServletResponse.SC_OK;

        public List<String> getRedirects() {
            return redirects;
        }

        public int getStatus() {
            return status;
        }
    }
}