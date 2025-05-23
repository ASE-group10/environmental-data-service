package com.example.environmental_data_service.config;

import com.example.environmental_data_service.model.AirQualityData;
import com.example.environmental_data_service.service.EnvironmentalDataService;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * Test configuration class for the environmental data service.
 * Provides mock beans and property values for testing without
 * requiring external dependencies like Pyroscope or OpenWeather API.
 */
@TestConfiguration
@Profile("test")
public class TestConfig {

    /**
     * Mock property values for testing.
     * These are automatically used instead of the real values in the test
     * environment.
     */
    @Bean
    @Primary
    @Profile("test")
    public RestTemplate restTemplate() {
        // Return a real RestTemplate that can be mocked in tests
        return new RestTemplate();
    }

    /**
     * Property value for the OpenWeather API key.
     * This allows tests to run without needing a real API key.
     */
    @Bean(name = "openweather.apikey")
    @Profile("test")
    public String openWeatherApiKey() {
        return "test-api-key";
    }

    /**
     * Creates a mock EnvironmentalDataService that doesn't call external APIs.
     * This bean is only used when explicitly imported in test classes that need it.
     */
    @Bean
    @Profile("test")
    public EnvironmentalDataService mockEnvironmentalDataService() {
        EnvironmentalDataService mockService = Mockito.mock(EnvironmentalDataService.class);

        // Set up default behavior for the mock service to return empty data
        AirQualityData mockData = new AirQualityData();
        mockData.setAqi(1); // Good air quality for testing

        Mockito.when(mockService.getAirQualityData(Mockito.anyList()))
                .thenReturn(Collections.singletonList(mockData));

        return mockService;
    }

    /**
     * Mock property values for testing.
     * These are automatically used instead of the real values in the test
     * environment.
     */
    @Bean
    @Primary
    @Profile("test")
    public PyroscopeBean pyroscopeBean() {
        return new MockPyroscopeBean();
    }

    /**
     * Mock implementation of PyroscopeBean for testing
     */
    public static class MockPyroscopeBean extends PyroscopeBean {
        public MockPyroscopeBean() {
            super("test", "test-app", "mock-address", "mock-user", "mock-password");
        }
    }
}