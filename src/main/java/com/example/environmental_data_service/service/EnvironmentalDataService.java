package com.example.environmental_data_service.service;

import com.example.environmental_data_service.model.FeatureCollection;
import com.example.environmental_data_service.entity.FeatureCollectionEntity;
import com.example.environmental_data_service.repo.FeatureCollectionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class EnvironmentalDataService {

    private static final Logger logger = LoggerFactory.getLogger(EnvironmentalDataService.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final FeatureCollectionRepository featureCollectionRepository;

    @Value("${geojson.url}")
    private String geoJsonUrl;

    public EnvironmentalDataService(RestTemplate restTemplate, ObjectMapper objectMapper, FeatureCollectionRepository featureCollectionRepository) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.featureCollectionRepository = featureCollectionRepository;
    }

    @Transactional
    public void saveGeoJsonData() {
        try {
            logger.info("Saving GeoJSON data to the database");
            FeatureCollection featureCollection = fetchGeoJsonData();
            FeatureCollectionEntity featureCollectionEntity = convertToFeatureCollectionEntity(featureCollection);
            featureCollectionRepository.save(featureCollectionEntity);
            logger.info("GeoJSON data saved successfully");
        } catch (IOException e) {
            logger.error("Error saving GeoJSON data", e);
            throw new RuntimeException("Failed to save GeoJSON data", e);
        }
    }

    public FeatureCollection fetchGeoJsonData() throws IOException {
        logger.info("Fetching GeoJSON data from {}", geoJsonUrl);
        String geoJsonResponse = restTemplate.getForObject(geoJsonUrl, String.class);
        if (geoJsonResponse == null || geoJsonResponse.isEmpty()) {
            throw new IllegalStateException("Fetched GeoJSON data is null or empty");
        }
        return objectMapper.readValue(geoJsonResponse, FeatureCollection.class);
    }

    private FeatureCollectionEntity convertToFeatureCollectionEntity(FeatureCollection featureCollection) {
        // Conversion logic (can be moved to a separate mapper class)
        FeatureCollectionEntity entity = new FeatureCollectionEntity();
        entity.setType(featureCollection.getType());
        entity.setName(featureCollection.getName());
        // Add conversion of features and CRS...
        return entity;
    }
}
