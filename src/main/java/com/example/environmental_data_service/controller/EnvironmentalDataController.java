package com.example.environmental_data_service.controller;

import com.example.environmental_data_service.model.EnvironmentalDataResponse;
import com.example.environmental_data_service.model.FeatureCollection;
import com.example.environmental_data_service.service.EnvironmentalDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class EnvironmentalDataController {

    private EnvironmentalDataService environmentalDataService;

    @Autowired
    public void GeoJsonController(EnvironmentalDataService environmentalDataService) {
        this.environmentalDataService = environmentalDataService;
    }

    @GetMapping("/geojson-data")
    public FeatureCollection getGeoJsonData() throws IOException {
        // Fetch and return GeoJSON data
        return environmentalDataService.fetchGeoJsonData();
    }
    @GetMapping("/hello-env")
    public String getHelloData() throws IOException {
        // Fetch and return GeoJSON data
        return "Hello from Environment Data Service!";
    }
}
