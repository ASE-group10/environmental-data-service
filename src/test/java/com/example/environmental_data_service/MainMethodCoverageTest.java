package com.example.environmental_data_service;

import com.example.environmental_data_service.config.AwsSecretsInitializer;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = { "spring.main.lazy-initialization=true" })
class MainMethodCoverageTest {

    @Test
    void mainMethodRunsSuccessfully() {
        // Prepare fake secrets JSON with necessary keys
        String fakeSecrets = """
        {
          "SPRING_DATASOURCE_URL": "jdbc:h2:mem:testdb",
          "SPRING_DATASOURCE_USERNAME": "sa",
          "openweather.apikey": "dummy-test-key"
        }
        """;

        try (MockedStatic<SecretsManagerClient> mockedStatic = Mockito.mockStatic(SecretsManagerClient.class)) {
            // Create a mock AWS SecretsManagerClient
            SecretsManagerClient mockClient = mock(SecretsManagerClient.class);
            mockedStatic.when(SecretsManagerClient::create).thenReturn(mockClient);

            // Prepare the fake response from AWS Secrets Manager
            GetSecretValueResponse fakeResponse = GetSecretValueResponse.builder()
                    .secretString(fakeSecrets)
                    .build();
            when(mockClient.getSecretValue(any(GetSecretValueRequest.class)))
                    .thenReturn(fakeResponse);

            // Pass an argument to override the default port.
            // Using "--server.port=0" makes Spring Boot use a random available port.
            EnvironmentalDataServiceApplication.main(new String[]{"--server.port=0"});
        }
    }
}
