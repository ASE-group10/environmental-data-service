package com.example.environmental_data_service.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

class AwsSecretsInitializerTest {

    @Test
    void testInitialize_skippedForLocalProfile() {
        GenericApplicationContext context = new GenericApplicationContext();
        context.getEnvironment().getSystemProperties().put("spring.profiles.active", "local");

        AwsSecretsInitializer initializer = new AwsSecretsInitializer();
        initializer.initialize(context);

        PropertySource<?> ps = context.getEnvironment().getPropertySources().get("aws-secrets");
        assertNull(ps, "PropertySource 'aws-secrets' should not be added when running in local mode");
    }

    @Test
    void testInitialize_successfulSecretInjection() {
        GenericApplicationContext context = new GenericApplicationContext();
        context.getEnvironment().getSystemProperties().put("spring.profiles.active", "dev");

        String secretJson = "{\"SPRING_DATASOURCE_URL\":\"jdbc:mysql://localhost:3306/db\",\"SPRING_DATASOURCE_USERNAME\":\"user\"}";

        try (MockedStatic<SecretsManagerClient> mockedStatic = Mockito.mockStatic(SecretsManagerClient.class)) {
            SecretsManagerClient mockClient = mock(SecretsManagerClient.class);
            mockedStatic.when(SecretsManagerClient::create).thenReturn(mockClient);

            GetSecretValueResponse fakeResponse = GetSecretValueResponse.builder()
                    .secretString(secretJson)
                    .build();
            when(mockClient.getSecretValue(any(GetSecretValueRequest.class))).thenReturn(fakeResponse);

            AwsSecretsInitializer initializer = new AwsSecretsInitializer();
            initializer.initialize(context);

            PropertySource<?> ps = context.getEnvironment().getPropertySources().get("aws-secrets");
            assertNotNull(ps);

            @SuppressWarnings("unchecked")
            MapPropertySource mapPs = (MapPropertySource) ps;
            Map<String, Object> props = mapPs.getSource();
            assertEquals("jdbc:mysql://localhost:3306/db", props.get("SPRING_DATASOURCE_URL"));
            assertEquals("user", props.get("SPRING_DATASOURCE_USERNAME"));

            assertEquals("jdbc:mysql://localhost:3306/db", context.getEnvironment().getProperty("SPRING_DATASOURCE_URL"));
            assertEquals("user", context.getEnvironment().getProperty("SPRING_DATASOURCE_USERNAME"));
        }
    }

    @Test
    void testInitialize_failureSecretInjection() {
        GenericApplicationContext context = new GenericApplicationContext();
        context.getEnvironment().getSystemProperties().put("spring.profiles.active", "dev");

        try (MockedStatic<SecretsManagerClient> mockedStatic = Mockito.mockStatic(SecretsManagerClient.class)) {
            SecretsManagerClient mockClient = mock(SecretsManagerClient.class);
            mockedStatic.when(SecretsManagerClient::create).thenReturn(mockClient);

            when(mockClient.getSecretValue(any(GetSecretValueRequest.class)))
                    .thenThrow(new RuntimeException("Simulated AWS failure"));

            AwsSecretsInitializer initializer = new AwsSecretsInitializer();

            RuntimeException ex = assertThrows(RuntimeException.class, () -> initializer.initialize(context));
            assertTrue(ex.getMessage().contains("Failed to load AWS Secrets"));
        }
    }
}
