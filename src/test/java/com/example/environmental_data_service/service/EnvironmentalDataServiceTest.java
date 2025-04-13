package com.example.environmental_data_service.service;

import com.example.environmental_data_service.model.AirQualityData;
import com.example.environmental_data_service.model.OpenWeatherResponse;
import com.example.environmental_data_service.model.Waypoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class EnvironmentalDataServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EnvironmentalDataService environmentalDataService;

    private final String TEST_API_KEY = "test-api-key";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(environmentalDataService, "openWeatherApiKey", TEST_API_KEY);
    }

    @Test
    void testGetAirQualityData_SingleWaypoint_Success() {
        // Arrange
        Waypoint waypoint = new Waypoint(51.5074, -0.1278); // London coordinates

        OpenWeatherResponse mockResponse = createMockOpenWeatherResponse(2); // AQI of 2 (Fair)

        when(restTemplate.getForEntity(contains("api.openweathermap.org"), eq(OpenWeatherResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // Act
        List<AirQualityData> result = environmentalDataService.getAirQualityData(Collections.singletonList(waypoint));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        AirQualityData data = result.get(0);
        assertEquals(2, data.getAqi());
        assertEquals(waypoint.getLatitude(), data.getLatitude());
        assertEquals(waypoint.getLongitude(), data.getLongitude());
        assertEquals(300.5, data.getCo());
        assertEquals(0.5, data.getNo());
        assertEquals(10.2, data.getNo2());
        assertEquals(55.3, data.getO3());
        assertEquals(7.5, data.getSo2());
        assertEquals(2.1, data.getPm2_5());
        assertEquals(15.3, data.getPm10());
        assertEquals(0.7, data.getNh3());

        verify(restTemplate, times(1)).getForEntity(anyString(), eq(OpenWeatherResponse.class));
    }

    @Test
    void testGetAirQualityData_MultipleWaypoints_Success() {
        // Arrange
        Waypoint waypoint1 = new Waypoint(51.5074, -0.1278); // London
        Waypoint waypoint2 = new Waypoint(48.8566, 2.3522); // Paris

        OpenWeatherResponse mockResponse1 = createMockOpenWeatherResponse(2); // AQI of 2 for London
        OpenWeatherResponse mockResponse2 = createMockOpenWeatherResponse(3); // AQI of 3 for Paris

        when(restTemplate.getForEntity(contains("lat=51.5074"), eq(OpenWeatherResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse1, HttpStatus.OK));

        when(restTemplate.getForEntity(contains("lat=48.8566"), eq(OpenWeatherResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse2, HttpStatus.OK));

        // Act
        List<AirQualityData> result = environmentalDataService.getAirQualityData(Arrays.asList(waypoint1, waypoint2));

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(2, result.get(0).getAqi());
        assertEquals(3, result.get(1).getAqi());

        verify(restTemplate, times(2)).getForEntity(anyString(), eq(OpenWeatherResponse.class));
    }

    @Test
    void testGetAirQualityData_ApiError() {
        // Arrange
        Waypoint waypoint = new Waypoint(51.5074, -0.1278);

        when(restTemplate.getForEntity(anyString(), eq(OpenWeatherResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Invalid API key"));

        // Act & Assert
        Exception exception = assertThrows(HttpClientErrorException.class, () -> {
            environmentalDataService.getAirQualityData(Collections.singletonList(waypoint));
        });

        assertTrue(exception.getMessage().contains("Invalid API key"));
    }

    @Test
    void testGetAirQualityData_NullResponse() {
        // Arrange
        Waypoint waypoint = new Waypoint(51.5074, -0.1278);

        when(restTemplate.getForEntity(anyString(), eq(OpenWeatherResponse.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        // Act
        List<AirQualityData> result = environmentalDataService.getAirQualityData(Collections.singletonList(waypoint));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        AirQualityData data = result.get(0);
        assertEquals(0, data.getAqi()); // Default value
        assertEquals(waypoint.getLatitude(), data.getLatitude());
        assertEquals(waypoint.getLongitude(), data.getLongitude());
    }

    @Test
    void testGetAirQualityData_EmptyListResponse() {
        // Arrange
        Waypoint waypoint = new Waypoint(51.5074, -0.1278);

        OpenWeatherResponse mockResponse = new OpenWeatherResponse();
        mockResponse.setList(Collections.emptyList()); // Empty list

        when(restTemplate.getForEntity(anyString(), eq(OpenWeatherResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // Act
        List<AirQualityData> result = environmentalDataService.getAirQualityData(Collections.singletonList(waypoint));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        AirQualityData data = result.get(0);
        assertEquals(0, data.getAqi()); // Default value
        assertEquals(waypoint.getLatitude(), data.getLatitude());
        assertEquals(waypoint.getLongitude(), data.getLongitude());
    }

    @Test
    void testGetAirQualityData_NullFields() {
        // Arrange
        Waypoint waypoint = new Waypoint(51.5074, -0.1278);

        OpenWeatherResponse mockResponse = new OpenWeatherResponse();
        OpenWeatherResponse.AirData airData = new OpenWeatherResponse.AirData();
        mockResponse.setList(Collections.singletonList(airData));
        // Main and components are null

        when(restTemplate.getForEntity(anyString(), eq(OpenWeatherResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // Act
        List<AirQualityData> result = environmentalDataService.getAirQualityData(Collections.singletonList(waypoint));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        AirQualityData data = result.get(0);
        assertEquals(0, data.getAqi()); // Default value
        assertEquals(waypoint.getLatitude(), data.getLatitude());
        assertEquals(waypoint.getLongitude(), data.getLongitude());
    }

    @Test
    void testGetAirQualityData_EmptyWaypointList() {
        // Act
        List<AirQualityData> result = environmentalDataService.getAirQualityData(Collections.emptyList());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(restTemplate, never()).getForEntity(anyString(), eq(OpenWeatherResponse.class));
    }

    /**
     * Helper method to create mock OpenWeather API response
     */
    private OpenWeatherResponse createMockOpenWeatherResponse(int aqi) {
        OpenWeatherResponse response = new OpenWeatherResponse();

        OpenWeatherResponse.AirData airData = new OpenWeatherResponse.AirData();
        OpenWeatherResponse.Main main = new OpenWeatherResponse.Main();
        main.setAqi(aqi);
        airData.setMain(main);

        OpenWeatherResponse.Components components = new OpenWeatherResponse.Components();
        components.setCo(300.5);
        components.setNo(0.5);
        components.setNo2(10.2);
        components.setO3(55.3);
        components.setSo2(7.5);
        components.setPm2_5(2.1);
        components.setPm10(15.3);
        components.setNh3(0.7);
        airData.setComponents(components);

        response.setList(Collections.singletonList(airData));

        return response;
    }

    @Test
    void testGetAirQualityData_NullListInResponse() {
        // Arrange
        Waypoint waypoint = new Waypoint(51.5074, -0.1278);

        OpenWeatherResponse responseWithNullList = new OpenWeatherResponse();
        responseWithNullList.setList(null); // Simulate null list

        when(restTemplate.getForEntity(anyString(), eq(OpenWeatherResponse.class)))
                .thenReturn(new ResponseEntity<>(responseWithNullList, HttpStatus.OK));

        // Act
        List<AirQualityData> result = environmentalDataService.getAirQualityData(Collections.singletonList(waypoint));

        // Assert
        assertEquals(1, result.size());
        AirQualityData data = result.get(0);
        assertEquals(0, data.getAqi()); // default
        assertEquals(waypoint.getLatitude(), data.getLatitude());
        assertEquals(waypoint.getLongitude(), data.getLongitude());
    }

}