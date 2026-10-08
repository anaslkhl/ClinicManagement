package clinicManagement.support;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * {@link ServletConfig} / {@link ServletContext} doubles so a servlet can be
 * initialised the same way {@code ApplicationListener} initialises it at
 * runtime: the {@code authService} attribute is published on the context.
 */
public final class StubServletConfig {

    private StubServletConfig() {
    }

    public static ServletConfig create(Object authService) {

        Map<String, Object> attributes = new LinkedHashMap<>();
        attributes.put("authService", authService);

        ServletContext context = (ServletContext) Proxy.newProxyInstance(
                StubServletConfig.class.getClassLoader(),
                new Class<?>[]{ServletContext.class},
                contextHandler(attributes)
        );

        return (ServletConfig) Proxy.newProxyInstance(
                StubServletConfig.class.getClassLoader(),
                new Class<?>[]{ServletConfig.class},
                (proxy, method, args) -> {

                    if ("getServletContext".equals(method.getName())) {
                        return context;
                    }
                    if ("getServletName".equals(method.getName())) {
                        return "userServlet";
                    }
                    if ("toString".equals(method.getName())) {
                        return "StubServletConfig";
                    }
                    if ("hashCode".equals(method.getName())) {
                        return System.identityHashCode(proxy);
                    }
                    if ("equals".equals(method.getName())) {
                        return proxy == args[0];
                    }
                    return null;
                }
        );
    }

    private static InvocationHandler contextHandler(Map<String, Object> attributes) {

        return (proxy, method, args) -> {

            switch (method.getName()) {

                case "getAttribute":
                    return attributes.get(args[0]);

                case "setAttribute":
                    attributes.put((String) args[0], args[1]);
                    return null;

                case "removeAttribute":
                    attributes.remove(args[0]);
                    return null;

                case "toString":
                    return "StubServletContext";

                case "hashCode":
                    return System.identityHashCode(proxy);

                case "equals":
                    return proxy == args[0];

                default:
                    return null;
            }
        };
    }
}