package com.example.environmental_data_service.controller;

import com.example.environmental_data_service.controller.EnvironmentalDataController;
import com.example.environmental_data_service.model.AirQualityData;
import com.example.environmental_data_service.model.Waypoint;
import com.example.environmental_data_service.service.EnvironmentalDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnvironmentalDataControllerTest {

    @Mock
    private EnvironmentalDataService environmentalDataService;

    @InjectMocks
    private EnvironmentalDataController environmentalDataController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAirQualityData_ValidWaypoints() {
        Waypoint waypoint = new Waypoint(0.0, 0.0);
        List<Waypoint> waypoints = Collections.singletonList(waypoint);

        // Create air quality data using setters instead of constructor
        AirQualityData airQualityData = new AirQualityData();
        airQualityData.setLatitude(0.0);
        airQualityData.setLongitude(0.0);
        airQualityData.setAqi(0);
        airQualityData.setCo(0.0);
        airQualityData.setNo(0.0);
        airQualityData.setNo2(0.0);
        airQualityData.setO3(0.0);
        airQualityData.setSo2(0.0);
        airQualityData.setPm2_5(0.0);
        airQualityData.setPm10(0.0);
        airQualityData.setNh3(0.0);

        when(environmentalDataService.getAirQualityData(waypoints))
                .thenReturn(Collections.singletonList(airQualityData));

        ResponseEntity<List<AirQualityData>> response = environmentalDataController.getAirQualityData(waypoints);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(airQualityData, response.getBody().get(0));
    }

    @Test
    void testGetAirQualityData_EmptyWaypoints() {
        List<Waypoint> waypoints = Collections.emptyList();
        when(environmentalDataService.getAirQualityData(waypoints)).thenReturn(Collections.emptyList());

        ResponseEntity<List<AirQualityData>> response = environmentalDataController.getAirQualityData(waypoints);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetAirQualityData_NullWaypoints() {
        assertThrows(NullPointerException.class, () -> {
            environmentalDataController.getAirQualityData(null);
        });
    }

    @Test
    void testGetAirQualityData_InvalidWaypointData() {
        Waypoint invalidWaypoint = new Waypoint(1000.0, 1000.0); // Invalid coordinates
        List<Waypoint> waypoints = Collections.singletonList(invalidWaypoint);
        when(environmentalDataService.getAirQualityData(waypoints))
                .thenThrow(new IllegalArgumentException("Invalid waypoint data"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            environmentalDataController.getAirQualityData(waypoints);
        });

        assertEquals("Invalid waypoint data", exception.getMessage());
    }
}
