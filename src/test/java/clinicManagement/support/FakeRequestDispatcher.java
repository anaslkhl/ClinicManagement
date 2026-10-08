package clinicManagement.support;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RequestDispatcher} double that records the paths forwarded to it.
 */
public class FakeRequestDispatcher implements RequestDispatcher {

    private final String path;

    private final List<String> forwardPaths = new ArrayList<>();

    private final List<String> includePaths = new ArrayList<>();

    public FakeRequestDispatcher(String path) {
        this.path = path;
    }

    @Override
    public void forward(ServletRequest request, ServletResponse response) {
        forwardPaths.add(path);
    }

    @Override
    public void include(ServletRequest request, ServletResponse response) {
        includePaths.add(path);
    }

    public List<String> getForwardPaths() {
        return forwardPaths;
    }
}