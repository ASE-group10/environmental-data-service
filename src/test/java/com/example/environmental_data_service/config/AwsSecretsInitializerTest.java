package com.example.environmental_data_service.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.Map;

public class AwsSecretsInitializerTest {

    @Test
    void testInitialize_skippedForLocalProfile() {
        // Arrange: Create a context with active profile "local"
        GenericApplicationContext context = new GenericApplicationContext();
        // Set 'spring.profiles.active' to "local" via system properties so it is visible in the environment
        context.getEnvironment().getSystemProperties().put("spring.profiles.active", "local");

        AwsSecretsInitializer initializer = new AwsSecretsInitializer();

        // Act: Call initialize; it should skip injecting AWS secrets.
        initializer.initialize(context);

        // Assert: No property source named "aws-secrets" should exist.
        PropertySource<?> ps = context.getEnvironment().getPropertySources().get("aws-secrets");
        assertNull(ps, "PropertySource 'aws-secrets' should not be added when running in local mode");
    }

    @Test
    void testInitialize_successfulSecretInjection() throws Exception {
        // Arrange: Create a context with an active profile that is not "local" (e.g., "dev")
        GenericApplicationContext context = new GenericApplicationContext();
        context.getEnvironment().getSystemProperties().put("spring.profiles.active", "dev");

        // Prepare a fake secret JSON (this is the format returned from AWS Secrets Manager)
        String secretJson = "{\"SPRING_DATASOURCE_URL\":\"jdbc:mysql://localhost:3306/db\",\"SPRING_DATASOURCE_USERNAME\":\"user\"}";

        // Use Mockito to mock static calls to SecretsManagerClient.create()
        try (MockedStatic<SecretsManagerClient> mockedStatic = Mockito.mockStatic(SecretsManagerClient.class)) {
            // Create a mock client
            SecretsManagerClient mockClient = mock(SecretsManagerClient.class);
            // When SecretsManagerClient.create() is called, return our mock
            mockedStatic.when(SecretsManagerClient::create).thenReturn(mockClient);

            // Prepare a fake response that returns our secret JSON
            GetSecretValueResponse fakeResponse = GetSecretValueResponse.builder()
                    .secretString(secretJson)
                    .build();
            when(mockClient.getSecretValue(any(GetSecretValueRequest.class))).thenReturn(fakeResponse);

            AwsSecretsInitializer initializer = new AwsSecretsInitializer();

            // Act: Initialize the context (this should inject the secrets)
            initializer.initialize(context);

            // Assert: Verify that a property source named "aws-secrets" was added with the expected keys.
            PropertySource<?> ps = context.getEnvironment().getPropertySources().get("aws-secrets");
            assertNotNull(ps, "PropertySource 'aws-secrets' should be added");

            // Cast to MapPropertySource and verify its content.
            @SuppressWarnings("unchecked")
            MapPropertySource mapPs = (MapPropertySource) ps;
            Map<String, Object> props = mapPs.getSource();
            assertEquals("jdbc:mysql://localhost:3306/db", props.get("SPRING_DATASOURCE_URL"));
            assertEquals("user", props.get("SPRING_DATASOURCE_USERNAME"));

            // Also verify that these properties are accessible through the environment.
            assertEquals("jdbc:mysql://localhost:3306/db", context.getEnvironment().getProperty("SPRING_DATASOURCE_URL"));
            assertEquals("user", context.getEnvironment().getProperty("SPRING_DATASOURCE_USERNAME"));
        }
    }

    @Test
    void testInitialize_failureSecretInjection() {
        // Arrange: Create a context with a non-local active profile.
        GenericApplicationContext context = new GenericApplicationContext();
        context.getEnvironment().getSystemProperties().put("spring.profiles.active", "dev");

        // Use Mockito to mock the static SecretsManagerClient.create()
        try (MockedStatic<SecretsManagerClient> mockedStatic = Mockito.mockStatic(SecretsManagerClient.class)) {
            SecretsManagerClient mockClient = mock(SecretsManagerClient.class);
            mockedStatic.when(SecretsManagerClient::create).thenReturn(mockClient);

            // Simulate a failure: When getSecretValue is called, throw an exception.
            when(mockClient.getSecretValue(any(GetSecretValueRequest.class)))
                    .thenThrow(new RuntimeException("Simulated AWS failure"));

            AwsSecretsInitializer initializer = new AwsSecretsInitializer();

            // Act & Assert: Expect the initializer to throw a RuntimeException with a message containing "Failed to load AWS Secrets"
            RuntimeException ex = assertThrows(RuntimeException.class, () -> initializer.initialize(context));
            assertTrue(ex.getMessage().contains("Failed to load AWS Secrets"),
                    "Exception message should indicate failure to load AWS Secrets");
        }
    }
}
