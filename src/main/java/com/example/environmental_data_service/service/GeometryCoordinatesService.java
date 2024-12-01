package com.example.environmental_data_service.service;

import com.example.environmental_data_service.model.GeometryCoordinates;
import com.example.environmental_data_service.repo.GeometryCoordinatesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
public class GeometryCoordinatesService {

    private static final Logger logger = LoggerFactory.getLogger(GeometryCoordinatesService.class);

    private final GeometryCoordinatesRepository repository;
    private final RestTemplate restTemplate;

    @Value("${route.calculation.service.url}")
    private String routeCalculationUrl;

    public GeometryCoordinatesService(GeometryCoordinatesRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    // Fetch all coordinates
    public List<GeometryCoordinates> getAllCoordinates() {
        logger.info("Fetching all coordinates.");
        return repository.findAll();
    }

    // Send coordinates to route-calculation service
    public String sendCoordinatesToRouteCalculation() {
        List<GeometryCoordinates> coordinates = getAllCoordinates();

        if (coordinates.isEmpty()) {
            logger.warn("No coordinates available to send.");
            return "No coordinates available to send.";
        }

        logger.info("Sending {} coordinates to Route Calculation Service.", coordinates.size());
        try {
            restTemplate.postForObject(routeCalculationUrl, coordinates, String.class);
            return "Coordinates successfully sent to Route Calculation Service.";
        } catch (Exception e) {
            logger.error("Failed to send data to Route Calculation Service", e);
            return "Failed to send coordinates. Please try again later.";
        }
    }
}
