package com.example.environmental_data_service.controller;

import com.example.environmental_data_service.model.AirQualityData;
import com.example.environmental_data_service.model.Waypoint;
import com.example.environmental_data_service.service.EnvironmentalDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/environmental-data")
@RequiredArgsConstructor
@Slf4j
public class EnvironmentalDataController {

    private final EnvironmentalDataService environmentalDataService;

    @PostMapping
    public ResponseEntity<List<AirQualityData>> getAirQualityData(@RequestBody List<Waypoint> waypoints) {
        // Log the received waypoints
        log.info("Received environmental data request with {} waypoints: {}", waypoints.size(), waypoints);

        List<AirQualityData> data = environmentalDataService.getAirQualityData(waypoints);

        // Log the response data
        log.info("Returning environmental data: {}", data);
        return ResponseEntity.ok(data);
    }
}
