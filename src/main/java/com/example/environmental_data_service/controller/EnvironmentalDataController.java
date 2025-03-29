package com.example.environmental_data_service.controller;


import com.example.environmental_data_service.model.AirQualityData;
import com.example.environmental_data_service.model.Waypoint;
import com.example.environmental_data_service.service.EnvironmentalDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/environmental-data")
@RequiredArgsConstructor
public class EnvironmentalDataController {

    private final EnvironmentalDataService environmentalDataService;

    @PostMapping
    public ResponseEntity<List<AirQualityData>> getAirQualityData(@RequestBody List<Waypoint> waypoints) {
        List<AirQualityData> data = environmentalDataService.getAirQualityData(waypoints);
        return ResponseEntity.ok(data);
    }
}
