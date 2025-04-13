package com.example.environmental_data_service.config;

import io.pyroscope.javaagent.PyroscopeAgent;
import io.pyroscope.javaagent.config.Config;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PyroscopeBeanTest {

    @Test
    void testSkipsOnWindows() {
        System.setProperty("os.name", "Windows 11");

        PyroscopeBean bean = new PyroscopeBean(
                "dev",
                "test-app",
                "http://pyro",
                "user",
                "pass"
        );

        bean.init(); // should skip due to OS
    }

    @Test
    void testSkipsOnTestProfile() {
        System.setProperty("os.name", "Linux");

        PyroscopeBean bean = new PyroscopeBean(
                "test",
                "test-app",
                "http://pyro",
                "user",
                "pass"
        );

        bean.init(); // should skip due to profile
    }

    @Test
    void testSkipsOnMissingCredentials() {
        System.setProperty("os.name", "Linux");

        PyroscopeBean bean = new PyroscopeBean(
                "dev",
                "test-app",
                "http://pyro",
                "",    // empty user
                ""     // empty password
        );

        bean.init(); // should skip due to missing creds
    }

    @Test
    void testStartsPyroscopeAgentSuccessfully() {
        System.setProperty("os.name", "Linux");

        PyroscopeBean bean = new PyroscopeBean(
                "prod",
                "myapp",
                "http://pyro",
                "admin",
                "securepass"
        );

        try (MockedStatic<PyroscopeAgent> mocked = Mockito.mockStatic(PyroscopeAgent.class)) {
            mocked.when(() -> PyroscopeAgent.start(any(Config.class)))
                    .thenAnswer(invocation -> {
                        System.out.println("✅ PyroscopeAgent.start() was called");
                        return null;
                    });

            bean.init();

            mocked.verify(() -> PyroscopeAgent.start(any(Config.class)), times(1));
        }
    }

    @Test
    void testHandlesExceptionGracefully() {
        System.setProperty("os.name", "Linux");

        PyroscopeBean bean = new PyroscopeBean(
                "prod",
                "myapp",
                "http://pyro",
                "admin",
                "securepass"
        );

        try (MockedStatic<PyroscopeAgent> mocked = Mockito.mockStatic(PyroscopeAgent.class)) {
            mocked.when(() -> PyroscopeAgent.start(any(Config.class)))
                    .thenThrow(new RuntimeException("Pyroscope failed"));

            bean.init(); // should catch and log the exception
        }
    }
}
