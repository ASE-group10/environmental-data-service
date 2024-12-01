package com.example.environmental_data_service.controller;

import com.example.environmental_data_service.model.FeatureCollection;
import com.example.environmental_data_service.model.GeometryCoordinates;
import com.example.environmental_data_service.service.EnvironmentalDataService;
import com.example.environmental_data_service.service.GeometryCoordinatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/environmental-data")
public class EnvironmentalDataController {

    private final EnvironmentalDataService environmentalDataService;
    private final GeometryCoordinatesService service;

    @Autowired
    public EnvironmentalDataController(EnvironmentalDataService environmentalDataService,
                                       GeometryCoordinatesService service) {
        this.environmentalDataService = environmentalDataService;
        this.service = service;
    }

    @GetMapping("/coordinates")
    public ResponseEntity<List<GeometryCoordinates>> getAllCoordinates() {
        try {
            List<GeometryCoordinates> coordinates = service.getAllCoordinates();
            return ResponseEntity.ok(coordinates);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendCoordinatesToRouteCalculation() {
        try {
            String response = service.sendCoordinatesToRouteCalculation();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send coordinates");
        }
    }

    @GetMapping("/geojson-data")
    public ResponseEntity<FeatureCollection> getGeoJsonData() {
        try {
            FeatureCollection data = environmentalDataService.fetchGeoJsonData();
            return ResponseEntity.ok(data);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/save-geojson")
    public ResponseEntity<String> saveGeoJsonData() {
        environmentalDataService.saveGeoJsonData();
        return ResponseEntity.ok("Data saved successfully");
    }

    @GetMapping("/hello-env")
    public ResponseEntity<String> getHelloData() {
        return ResponseEntity.ok("Hello from Environment Data Service!");
    }
}
